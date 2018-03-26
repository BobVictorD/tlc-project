package tlc.project.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import tlc.project.model.Advertisement;
import tlc.project.service.AdvertisementService;

@WebServlet(
		name = "ApiAdvertisementServletV2",
		urlPatterns = {"/api/advertisementV2"}
		)
public class ApiAdvertisementV2Servlet  extends HttpServlet {
private static final long serialVersionUID = 1L;
	
	ObjectMapper mapper = new ObjectMapper();
	AdvertisementService advService = AdvertisementService.getInstance();
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		System.out.println("GET");

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
		String searchByTitle = null;
		Long searchByPriceMin = null;
		Long searchByPriceMax = null;
		Date searchByDateMin = null;
		Date searchByDateMax = null;
		searchByTitle = request.getParameter("searchByTitle");
		System.out.println("	searchByTitle : " + searchByTitle);
		String param = request.getParameter("searchByPriceMin");
		System.out.println("	searchByPriceMin : " + param);
		if (param != null && !param.isEmpty()) {
			searchByPriceMin = Long.decode(param);
		}
		param = request.getParameter("searchByPriceMax");
		System.out.println("	searchByPriceMax : " + param);		
		if (param != null && !param.isEmpty()) {
			searchByPriceMax = Long.decode(param);
		}
		param = request.getParameter("searchByDateMin");
		System.out.println("	searchByDateMin : " + param);	
		if (param != null && !param.isEmpty()) {
			try {
				searchByDateMin = dateFormater.parse(param);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		param = request.getParameter("searchByDateMax");
		System.out.println("	searchByDateMax : " + param);
		if (param != null && !param.isEmpty()) {
			try {
				searchByDateMax = dateFormater.parse(param);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Advertisement> advertisements = advService.get(searchByTitle, searchByPriceMin, searchByPriceMax,searchByDateMin, searchByDateMax);
		if (advertisements != null)
			System.out.println(advertisements.size() + " elements found");
		else
			System.out.println("0 element found");
		response.sendRedirect("/home.jsp");

	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		System.out.println("POST");

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");		
		String content = request.getParameter("content");
		
		List<Advertisement> advs = mapper.readValue(content, new TypeReference<List<Advertisement>>(){});
		advs.stream().forEach(p->p.setDate(new Date()));
		advService.add(advs);

		response.sendRedirect("/home.jsp");

	}
	
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		System.out.println("DELETE");

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String content = request.getParameter("content");

		System.out.println(content);
		List<Advertisement> advs = mapper.readValue(content, new TypeReference<List<Advertisement>>(){});
		advService.delete(advs);
		
		response.sendRedirect("/home.jsp");

	}
}
