package servlets;

import com.tutorialspoint.Counter;
import com.tutorialspoint.MyThread;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HelloWorldServlet")
public class HelloWorldServlet extends HttpServlet {

    int classNotStaticVariable = 0;
    static int classStaticVariable = 0;
    Object monitor = new Object();

    public HelloWorldServlet() {
        System.out.println("HelloWorldServlet controller");
    }
    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        super.doPost(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        //Step 1: set the content type
        response.setContentType("text/html");

        //Step 2: get the printwriter
        PrintWriter out = response.getWriter();

        //Step 3: generate HTML content
        out.println("<html><body>");

        out.println("<h2>Hello World</h2>");
        out.println("<hr>");
        out.println("<h2>Time on the server is: " + new java.util.Date() + "</h2>");

        //Outside threads and counter to transfer
        MyThread[] myThreads = new MyThread[1000];
        Counter counter = new Counter();

        //Inner threads
        MyInnerThread[] myInnerThreads = new MyInnerThread[1000];

        //start variables values

        //Outer variables
        out.println("<h2>Not static outer variable is " + counter.getNotStaticVar() + "</h2>");

        out.println("<h2>Static outer variable is " + Counter.getStaticVar() + "</h2>");

        //Inner variables
        out.println("<h2>Not static class variable is " + classNotStaticVariable + "</h2>");

        out.println("<h2>Static class variable is " + classStaticVariable + "</h2>");

        out.println("<hr>");

        //CountDownLatch latch = new CountDownLatch(myThreads.length);
        for(int i = 0; i<myThreads.length; i++) {
            myThreads[i] = new MyThread(counter);
            myThreads[i].start();
            myInnerThreads[i] = new MyInnerThread();
            myInnerThreads[i].start();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        out.println("<h2>Class not static class variable is " + counter.getNotStaticVar() + "</h2>");

        out.println("<h2>Class static class variable is " + Counter.getStaticVar() + "</h2>");

        out.println("<h2>Inner not static variable is " + classNotStaticVariable + "</h2>");

        out.println("<h2>Inner static class variable is " + classStaticVariable + "</h2>");

        out.println("</body></html>");
    }

    class MyInnerThread extends Thread {

        @Override
        public  void run() {
            synchronized(monitor) {
                System.out.println("Thread " + Thread.currentThread().getName() + " innerNotStaticVariable before reading=" + classNotStaticVariable);
                int local = classNotStaticVariable;
                System.out.println("Thread " + Thread.currentThread().getName() + " innerNotStaticVariable after reading=" + classNotStaticVariable);
                System.out.println("Thread " + Thread.currentThread().getName() + " local=" + local);
                classNotStaticVariable = local + 1;
                System.out.println("Thread " + Thread.currentThread().getName() + " innerNotStaticVariable after modificatioin =" + classNotStaticVariable);

                System.out.println("Thread " + Thread.currentThread().getName() + " innerStaticVariable before reading=" + classStaticVariable);
                int local2 = classStaticVariable;
                System.out.println("Thread " + Thread.currentThread().getName() + " innerStaticVariable after reading=" + classStaticVariable);
                System.out.println("Thread " + Thread.currentThread().getName() + " local2=" + local2);
                classStaticVariable = local2 + 1;
                System.out.println("Thread " + Thread.currentThread().getName() + " innerStaticVariable after modification =" + classStaticVariable);
            }
        }
    }
}
