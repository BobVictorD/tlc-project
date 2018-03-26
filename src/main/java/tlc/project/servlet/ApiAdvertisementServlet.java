package tlc.project.servlet;

import java.io.IOException;
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
		name = "ApiAdvertisementServlet",
		urlPatterns = {"/api/advertisement"}
		)
public class ApiAdvertisementServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	ObjectMapper mapper = new ObjectMapper();
	AdvertisementService advService = AdvertisementService.getInstance();
	
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
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
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