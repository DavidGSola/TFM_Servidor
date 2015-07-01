import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

public class Lanzador extends HttpServlet {
	
	String mensajeServidor;

	public void init() throws ServletException
	{
		mensajeServidor = "Algoritmo Ejecutado";
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException 
	{
		System.out.println("------ POST ---------");
		try 
		{
			List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
			for (FileItem item : items)
			{
				if (!item.isFormField()) 
				{
					// Procesar form file field (input type="file").
					String fieldName = item.getFieldName();
					String fileName = FilenameUtils.getName(item.getName());
					InputStream fileContent = item.getInputStream();

					OutputStream os = new FileOutputStream("c:/temp/prueba.jpg");

					byte[] b = new byte[2048];
					int length;

					while ((length = fileContent.read(b)) != -1)
						os.write(b, 0, length);

					fileContent.close();
					os.close();
		        		
					int indexMejor = lanzarAlgoritmo();
					
					if(indexMejor >= 0)
					{
						String rutaMejor = "C:" + File.separator + "Users" + File.separator + "DavidGSola" + File.separator + "Documents" 
								+ File.separator + "Facultad" + File.separator + "Eclipse" + File.separator + "workspace" + File.separator + "ImageComparator" 
								+ File.separator + "Images" + File.separator + "models" + File.separator + fileName + File.separator + "0_" + indexMejor + "_test.bmp";
						
						response.setContentType("image/jpeg");

						File f = new File(rutaMejor);
						BufferedImage bi = ImageIO.read(f);
						OutputStream out = response.getOutputStream();
						ImageIO.write(bi, "png", out);
						out.close();
					}	
					
				}
			}
		} catch (FileUploadException e) 
		{
			throw new ServletException("Cannot parse multipart request.", e);
		}
	}
	
	public int lanzarAlgoritmo()
	{
		String arg_1 = "C:" + File.separator + "temp" + File.separator + "prueba.jpg";
		String ruta = "C:" + File.separator + "Users" + File.separator + "DavidGSola" + File.separator + "Documents" 
					+ File.separator + "Facultad" + File.separator + "Eclipse" + File.separator + "workspace" + File.separator + "TFM_Comparador" + File.separator + "Debug" + File.separator;
		String exe = "TFM_Comparador.exe";
			
		try 
		{
			Process p = new ProcessBuilder(ruta+exe, arg_1).start();
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line;
			while ((line = input.readLine()) != null)
				System.out.println(line);

			int res = p.exitValue();
			System.out.println(res);
			return res;
				
		} catch (IOException e) 
		{
			e.printStackTrace();
			return -1;
		}
	}

	public void destroy()
	{
		// do nothing.
	}
}
