package servlets;

import com.tutorialspoint.MyThread;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;

@WebServlet("/Multithreading")
public class Multithreading extends HttpServlet {

    public Multithreading() {
        System.out.println("Multithreading constructor");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        //Step 1: set the content type
        response.setContentType("text/html");

        //Step 2: get the printwriter
        PrintWriter out = response.getWriter();

        //Step 3: generate HTML content
        out.println("<html><body>");

        out.println("This is main thread");
        MyThread[] counters = new MyThread[2000];
        CountDownLatch latch = new CountDownLatch(counters.length);
        for(int i = 0; i<counters.length; i++) {
            counters[i].start();
        }

        //out.println(MyThread.getVar());

        out.println("</body></html>");


    }

}
