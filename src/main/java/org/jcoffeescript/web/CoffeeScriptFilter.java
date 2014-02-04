package org.jcoffeescript.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jcoffeescript.JCoffeeScriptCompileException;
import org.jcoffeescript.JCoffeeScriptCompiler;

public class CoffeeScriptFilter implements Filter {

	private static final String DEFAULT_JS_PREFIX = "/js";
	private static final String DEFAULT_COFFEE_PREFIX = "/WEB-INF/coffee";
	private static final int BUFFER_SIZE = 4096;
	private static final int MAX_COMPILED_JS = 100;
	
	private String javascriptResourcePrefix;
	private String coffeescriptFilenamePrefix;
	
	private long startupTime = new java.util.Date().getTime() / 1000 * 1000;

	@SuppressWarnings("serial")
	@Override
	public void init(FilterConfig config) throws ServletException {
		// prefixes
		String jsPrefix = config.getInitParameter("javascriptResourcePrefix");
		if (jsPrefix == null || jsPrefix.equals("")) {
			jsPrefix = DEFAULT_JS_PREFIX;
		}
		this.javascriptResourcePrefix = jsPrefix;
		
		String csPrefix = config.getInitParameter("coffeescriptFilenamePrefix");
		if (csPrefix == null || csPrefix.equals("")) {
			csPrefix = DEFAULT_COFFEE_PREFIX;
		}
		this.coffeescriptFilenamePrefix = csPrefix;
				
		// coffeescript compiler
		JCoffeeScriptCompiler compiler = new JCoffeeScriptCompiler();
		config.getServletContext().setAttribute("compiler", compiler);
		
		// hashmap
		LinkedHashMap<String, Binary> previousBinaries = new LinkedHashMap<String, Binary>() {

			@Override
			protected boolean removeEldestEntry(Entry<String, Binary> eldest) {
				return size() > MAX_COMPILED_JS;
			}
		};
		config.getServletContext().setAttribute("previousBinaries", Collections.synchronizedMap(previousBinaries));
	}
	
	
	@Override
	public void destroy() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		ServletContext servletContext = request.getSession().getServletContext();
		
		JCoffeeScriptCompiler compiler = (JCoffeeScriptCompiler) servletContext.getAttribute("compiler");
		Map<String, Binary> previousBinaries = (Map<String, Binary>) servletContext.getAttribute("previousBinaries");
		
		
		String javascriptURI = getJavascriptURI(request);
		String coffeeFilename = discoverCoffeeFilename(javascriptURI);
		if (coffeeFilename == null) {
			// it's not javascript
			chain.doFilter(req, resp);
			return;
		}
		
		URL coffeeURL = servletContext.getResource(coffeeFilename);
		
		if (coffeeURL == null) {
			// static javascript or coffe filename was deleted
			previousBinaries.remove(coffeeFilename);
			chain.doFilter(req, resp);
			return;
		}
		
		Binary binary;
		if ((binary = previousBinaries.get(coffeeFilename)) == null
				|| binary.isOlderThan(coffeeURL)) {
			
			InputStream coffeeStream = coffeeURL.openStream();
			String source = getContent(coffeeStream);

			try {
				synchronized (compiler) {
					binary = new Binary(coffeeURL, compiler.compile(source));
				}
			} catch (JCoffeeScriptCompileException e) {
				throw new ServletException("Compilation error on file: " + coffeeFilename + " " + e.getMessage(), e);
			}
		}
		
		// write in all cases to put value at the end of the linked list
		previousBinaries.put(coffeeFilename, binary);
		
		// optimizing response
		long lastModified = binary.getLastModified() == 0 ? this.startupTime : (binary.getLastModified() / 1000 * 1000);
		long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		
		if (lastModified > ifModifiedSince) {
			
			// write last modified info
			response.setDateHeader("Last-Modified", lastModified);
			response.setContentType("text/javascript");
			PrintWriter writer = resp.getWriter();
			writer.write(binary.getContent());
		} else {
			response.setContentLength(0);
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		}
	}
	
	private String getJavascriptURI(HttpServletRequest request) {
		String javascriptURI = request.getServletPath();
		if (request.getPathInfo() != null) {
			javascriptURI += request.getPathInfo();
		}
		return javascriptURI;
	}

	private String discoverCoffeeFilename(String javascriptResource) {

		Pattern pattern = Pattern.compile(javascriptResourcePrefix + "/(.*).js");
		Matcher matcher = pattern.matcher(javascriptResource);
		if (!matcher.matches()) {
			return null;
		}
		return coffeescriptFilenamePrefix + "/" + matcher.group(1) + ".coffee";
	}

	private String getContent(InputStream coffeeStream) throws IOException {
		InputStreamReader reader = new InputStreamReader(coffeeStream);
		try {
			StringWriter writer = new StringWriter();

			char[] buffer = new char[BUFFER_SIZE];
			int n = 0;
			while (-1 != (n = reader.read(buffer))) {
				writer.write(buffer, 0, n);
			}
			String source = writer.toString();
			return source;
		} finally {
			reader.close();
		}
	}

}
