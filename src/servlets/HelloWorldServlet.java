package servlets;

import com.tutorialspoint.Counter;
import com.tutorialspoint.MyThread;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.*;
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

        makeCounters(out);

        out.println("<hr>");

        makeSingleThreadPull(out);

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

    private void makeCounters(PrintWriter out) {
        //counter
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

        //start threads
        for (int i = 0; i < myThreads.length; i++) {
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
    }

    private void makeSingleThreadPull(PrintWriter out) {
        //executors
        Executor executor = null;
        ExecutorService service = null;

        Future submitted1 = null;
        Future submitted2 = null;
        Future submitted3 = null;
        Future submittedCallable = null;

        try {
            service = Executors.newSingleThreadExecutor();
            out.println("<h2>service.isShutDown before submitting tasks is " + service.isShutdown() + "</h2>");
            out.println("<h2>service.isTerminated before submitting tasks is " + service.isTerminated() + "</h2>");

            //submitting tasks
            Callable<Integer> callable = () -> {
                out.println("<h2>submitted task Callable</h2>");
                return 5;
            };
            service.execute(() -> {out.println("<h2>executed task 1</h2>");});
            //put Future into var
            submitted1 = service.submit(() -> {out.println("<h2>submitted task-1 2</h2>");}, 50);
            service.execute(() -> {out.println("<h2>executed task 3</h2>");});
            service.execute(() -> {out.println("<h2>executed task 4</h2>");});
            //put Future into var
            submittedCallable = service.submit(callable);
            //put Future into var
            submitted2 = service.submit(() -> {out.println("<h2>submitted task-2 5</h2>");}, 100L);
            service.execute(() -> {out.println("<h2>executed task 6</h2>");});
            service.execute(() -> {out.println("<h2>executed task 7</h2>");});
            //put Future into var
            submitted3 = service.submit(() -> {out.println("<h2>submitted task-3 8</h2>");}, (short)25);
            //Thread.sleep(2000);
        } /* catch (InterruptedException e) {
            e.printStackTrace();
        }*/ finally {
            out.println("<h2>-----------------------------------------------------------</h2>");
            out.println("<h2>service.isShutDown in finally block before shutdown is " + service.isShutdown() + "</h2>");
            out.println("<h2>service.isTerminated in finally block before shutdown is " + service.isTerminated() + "</h2>");

            try {
                out.println("<h2>-----------------------------------------------------------</h2>");
                out.println("<h2>submitted1.isCancelled in finally block before shutdown and getting value is " + submitted1.isCancelled() + "</h2>");
                out.println("<h2>submitted1.isDone in finally block before shutdown and getting value is " + submitted1.isDone() + "</h2>");
                out.println("<h2>submitted1.get in finally block before shutdown is " + submitted1.get() + "</h2>");

                out.println("<h2>submitted2.isCancelled in finally block before shutdown and getting value is " + submitted2.isCancelled() + "</h2>");
                out.println("<h2>submitted1.isDone in finally block before shutdown and getting value is " + submitted1.isDone() + "</h2>");
                out.println("<h2>submitted2.get in finally block before shutdown is " + submitted2.get() + "</h2>");

                out.println("<h2>submitted3.isCancelled in finally block before shutdown and getting value is " + submitted3.isCancelled() + "</h2>");
                out.println("<h2>submitted1.isDone in finally block before shutdown and getting value is " + submitted1.isDone() + "</h2>");
                out.println("<h2>submitted3.get in finally block before shutdown is " + submitted3.get() + "</h2>");

                out.println("<h2>submittedCallable.isCancelled in finally block before shutdown and getting value is " + submittedCallable.isCancelled() + "</h2>");
                out.println("<h2>submitted1.isDone in finally block before shutdown and getting value is " + submitted1.isDone() + "</h2>");
                out.println("<h2>submittedCallable.get in finally block before shutdown is " + submittedCallable.get() + "</h2>");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if(service != null) {
                /*try {
                    //service.shutdown();
                    System.out.println("Before awaitTermination");
                    service.awaitTermination(5, TimeUnit.SECONDS);
                    System.out.println("After awaitTermination");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                List<Runnable> futures = service.shutdownNow();
                //futures.forEach(future -> System.out.println("Futures are " + future));
                futures.forEach(Runnable::run);
            }

            try {
                out.println("<h2>-----------------------------------------------------------</h2>");
                out.println("<h2>service.isShutDown in finally block after shutdown is " + service.isShutdown() + "</h2>");
                out.println("<h2>service.isTerminated in finally block after shutdown is " + service.isTerminated() + "</h2>");

                out.println("<h2>-----------------------------------------------------------</h2>");
                out.println("<h2>submitted1.isCancelled in finally block after shutdown is " + submitted1.isCancelled() + "</h2>");
                out.println("<h2>submitted1.isDone in finally block after shutdown is " + submitted1.isDone() + "</h2>");
                out.println("<h2>submitted1.get in finally block after shutdown is " + submitted1.get() + "</h2>");

                out.println("<h2>submitted2.isCancelled in finally block after shutdown is " + submitted2.isCancelled() + "</h2>");
                out.println("<h2>submitted2.isDone in finally block after shutdown is " + submitted2.isDone() + "</h2>");
                out.println("<h2>submitted2.get in finally block after shutdown is " + submitted2.get() + "</h2>");

                out.println("<h2>submitted3.isCancelled in finally block after shutdown is " + submitted3.isCancelled() + "</h2>");
                out.println("<h2>submitted3.isDone in finally block after shutdown is " + submitted3.isDone() + "</h2>");
                out.println("<h2>submitted3.get in finally block after shutdown is " + submitted3.get() + "</h2>");

                out.println("<h2>submittedCallable.isCancelled in finally block after shutdown is " + submittedCallable.isCancelled() + "</h2>");
                out.println("<h2>submittedCallable.isDone in finally block after shutdown is " + submittedCallable.isDone() + "</h2>");
                out.println("<h2>submittedCallable.get in finally block after shutdown is " + submittedCallable.get() + "</h2>");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void makeNewSingleScheduledExecutor(PrintWriter out) {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    }
}
