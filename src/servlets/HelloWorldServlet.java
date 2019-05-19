package servlets;

import com.tutorialspoint.CountSumWeightAnimals;
import com.tutorialspoint.Counter;
import com.tutorialspoint.MyThread;
import com.tutorialspoint.WeightAnimals;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

        /*out.println("<hr>");

        makeCounters(out);

        out.println("<hr>");

        makeExecutorService(out);

        out.println("<hr>");

        makeScheduledExecutorService();*/

        //out.println("<hr>");

        //workWithConcurrentCollections(out);

        //workWithStreams(out);

        //workWithCyclicBareer(out);

        //workWithForkJoinPool(out);

        //workWithForkJoinTask(out);

        workWithSemaphore(out);

        out.println("<hr>");

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

    protected void makeCounters(PrintWriter out) {
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
        out.println("<h2>Not static outer variable is " + classNotStaticVariable + "</h2>");

        out.println("<h2>Static outer variable is " + classStaticVariable + "</h2>");

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

    protected void makeExecutorService(PrintWriter out) {
        ExecutorService service = null;

        Future submitted1 = null;
        Future submitted2 = null;
        Future submitted3 = null;
        Future submittedCallable = null;

        try {
            service = Executors.newFixedThreadPool(2);
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
            //print info about service and futures before shutdown
            printInfoAboutFutures(out, service, "before", Arrays.asList(submitted1, submitted2, submitted3, submittedCallable));

            if(service != null) {
                /*try {
                    service.shutdown();
                    out.println("Before awaitTermination service.isTerminated " + service.isTerminated());
                    service.awaitTermination(5, TimeUnit.SECONDS);
                    out.println("After awaitTermination service.isTerminated " + service.isTerminated());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                List<Runnable> futures = service.shutdownNow();
                //futures.forEach(future -> System.out.println("Futures are " + future));
                futures.forEach(Runnable::run);
            }

            //print info about service and futures after shutdown
            printInfoAboutFutures(out, service, "after", Arrays.asList(submitted1, submitted2, submitted3, submittedCallable));
        }
    }

    private void printInfoAboutFutures(PrintWriter out, ExecutorService service, String preposition, List<Future> futures) {

        if(service != null) {
            out.println("<h2>-----------------------------------------------------------</h2>");
            out.println("<h2>service.isShutDown in finally block " + preposition + " shutdown is " + service.isShutdown() + "</h2>");
            out.println("<h2>service.isTerminated in finally block " + preposition + " shutdown is " + service.isTerminated() + "</h2>");
        }

         if(futures != null && futures.size() != 0) {
             out.println("<h2>-----------------------------------------------------------</h2>");
             for(int i = 0; i<futures.size(); i++) {
                 Future future = futures.get(i);
                 out.println("<h2>submitted" + (i+1) + ".isCancelled in finally block " + preposition + " shutdown is " + future.isCancelled() + "</h2>");
                 out.println("<h2>submitted" + (i+1) + ".isDone in finally block " + preposition + " shutdown is " + future.isDone() + "</h2>");
                 try {
                     out.println("<h2>submitted" + (i+1) + ".get in finally block " + preposition + " shutdown is " + future.get() + "</h2>");
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 } catch (ExecutionException e) {
                     e.printStackTrace();
                 }
             }
         }
    }

    protected void makeScheduledExecutorService() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                System.out.println("This is callable for ScheduledExecutorService, delay = 2 seconds, Time on the server is: " + new java.util.Date());
                return 150;
            }
        };

        System.out.println("Time on the server is: " + new java.util.Date());
        service.schedule(callable, 2, TimeUnit.SECONDS);
        service.schedule(() -> {System.out.println("This is runnable for schedule in ScheduledExecutorService delay = 3 seconds. Time on the server is: " + new java.util.Date());}, 3, TimeUnit.SECONDS);
        service.scheduleAtFixedRate(() -> {System.out.println("This is runnable for scheduleAtFixedRate in ScheduledExecutorService, delay = 5 seconds, period = 3 seconds. Time on the server is: " + new java.util.Date());
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("This is end of runnable for scheduleAtFixedRate in ScheduledExecutorService.  Time on the server is: " + new java.util.Date());
        }, 5,3, TimeUnit.SECONDS);
        service.scheduleWithFixedDelay(() -> {System.out.println("This is runnable for scheduleWithFixedDelay in ScheduledExecutorService, delay = 5 seconds, delay = 3 seconds. Time on the server is: " + new java.util.Date());
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("This is end of runnable for scheduleWithFixedDelay in ScheduledExecutorService Time on the server is: " + new java.util.Date());
            }, 5,3, TimeUnit.SECONDS);
    }

    protected void workWithConcurrentCollections(PrintWriter out) {
        out.println("Working with ConcurrentHashMap");
        out.println("<hr>");
        Map<String, Integer> studentNotes = new ConcurrentHashMap<>();
        workWithConcurrentMap(out, studentNotes);
        for(String key: studentNotes.keySet()) {
            out.println("Key is " + key + ", value is " + studentNotes.get(key));
            out.println("<hr>");
            studentNotes.put(key+"2",2);
        }
        studentNotes.forEach((k, v) -> {
            out.println(k+" "+v);
            out.println("<hr>");
        });

        out.println("Working with ConcurrentLinkedQueue");
        out.println("<hr>");
        Queue<String> concurrentQueue = new ConcurrentLinkedQueue<>();
        workWithConcurrentLinkedQueue(out, concurrentQueue);
        concurrentQueue.forEach(x -> {
            out.println(x);
            out.println("<hr>");
        });

        out.println("Working with ConcurrentLinkedDeque");
        out.println("<hr>");
        Deque<String> concurrentDeque = new ConcurrentLinkedDeque<>();
        workWithConcurrentLinkedDeque(out, concurrentDeque);
        concurrentDeque.forEach(x -> {
            out.println(x);
            out.println("<hr>");
        });

        out.println("Working with BlockingQueue");
        out.println("<hr>");
        BlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(2);
        workWithBlockingQueue(out, arrayBlockingQueue);
        out.println("ArrayBlockingQueue contains ");
        arrayBlockingQueue.forEach(x -> {
            out.println(x +" ");
        });
        out.println("<hr>");

        out.println("Working with BlockingDeque");
        out.println("<hr>");
        BlockingDeque<String> linkedBlockingDeque = new LinkedBlockingDeque<>(2);
        workWithBlockingDeque(out, linkedBlockingDeque);
        out.println("ArrayBlockingDeque contains ");
        linkedBlockingDeque.forEach(x -> {
            out.println(x + " ");
        });
        out.println("<hr>");

        out.println("Working with concurrentSkipListSet");
        out.println("<hr>");
        Set<String> concurrentSkipListSet = new ConcurrentSkipListSet();
        workWithConcurrentSkipListSet(out, concurrentSkipListSet);
        concurrentSkipListSet.forEach(x -> {
            out.println(x);
            out.println("<hr>");
        });

        out.println("Working with concurrentSkipListMap");
        out.println("<hr>");
        Map<String, Integer> concurrentSkipListMap = new ConcurrentSkipListMap();
        workWithConcurrentMap(out, concurrentSkipListMap);
        concurrentSkipListMap.forEach((k, v) -> {
            out.println(k+" "+v);
            out.println("<hr>");
        });

        out.println("Working with CopyOnWriteArrayList");
        out.println("<hr>");
        List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        workWithcopyOnWriteArrayList(out, copyOnWriteArrayList);
        copyOnWriteArrayList.forEach(x -> {
            out.println(x);
            out.println("<hr>");
        });

        out.println("Working with CopyOnWriteArraySet");
        out.println("<hr>");
        Set<String> copyOnWriteArraySet = new CopyOnWriteArraySet<>();
        workWithcopyOnWriteArraySet(out, copyOnWriteArraySet);
        copyOnWriteArraySet.forEach(x -> {
            out.println(x);
            out.println("<hr>");
        });


    }

    private void workWithConcurrentMap(PrintWriter out, Map map) {
        Queue<String> students = new ArrayDeque<>(5);
        students.addAll(Arrays.asList("Chabakauri", "Jurov", "Volkov", "Birya", "Kater"));
        Queue<Integer> notes = new ArrayDeque<>(5);
        notes.addAll(Arrays.asList(1, 2, 3, 4, 5));
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i  = 0; i<5; i++) {
            service.submit(() -> {
                String student = students.poll();
                int note = notes.poll();
                map.put(student, note);
            });
        }
        service.shutdown();
        while (!service.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void workWithConcurrentLinkedQueue(PrintWriter out, Queue queue) {
        Queue<String> students = new ArrayDeque<>(5);
        students.addAll(Arrays.asList("Chabakauri", "Jurov", "Volkov", "Birya", "Kater", "Zheka"));
        ExecutorService serviceToAdd = Executors.newFixedThreadPool(6);
        for (int i  = 0; i<6; i++) {
            serviceToAdd.submit(() -> {
                String student = students.poll();
                queue.offer(student);
            });
        }
        serviceToAdd.shutdown();
        while (!serviceToAdd.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        queue.forEach(x -> {
            out.println(x);
            out.println("<hr>");
        });

        ExecutorService serviceToRemove = Executors.newFixedThreadPool(5);
        for (int i  = 0; i<5; i++) {
            serviceToRemove.submit(() -> {
                out.println("peek "+queue.peek());
                out.println("poll "+queue.poll());
            });
        }
        serviceToRemove.shutdown();
        while (!serviceToRemove.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        out.println("<hr>");
    }

    private void workWithConcurrentLinkedDeque(PrintWriter out, Deque deque) {
        Queue<String> students = new ArrayDeque<>(5);
        students.addAll(Arrays.asList("Chabakauri", "Jurov", "Volkov", "Birya", "Kater", "Zheka"));
        ExecutorService serviceToAdd = Executors.newFixedThreadPool(6);
        for (int i  = 0; i<6; i++) {
            int iForLambda = i;
            serviceToAdd.submit(() -> {
                String student = students.poll();
                if(iForLambda < 3) {
                    deque.offer(student);
                } else {
                    deque.push(student);
                }
            });
        }
        serviceToAdd.shutdown();
        while (!serviceToAdd.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        deque.forEach(x -> {
            out.println(x);
            out.println("<hr>");
        });

        ExecutorService serviceToRemove = Executors.newFixedThreadPool(5);
        for (int i  = 0; i<5; i++) {
            serviceToRemove.submit(() -> {
                out.println("peek "+deque.peek());
                out.println("pop "+deque.pop());
            });
        }
        serviceToRemove.shutdown();
        while (!serviceToRemove.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        out.println("<hr>");
    }

    private void workWithBlockingQueue(PrintWriter out, BlockingQueue blockingQueue) {
        class Adder extends Thread {
            @Override
            public void run() {
                try {
                    blockingQueue.offer("Sasha", 2, TimeUnit.SECONDS);
                    blockingQueue.offer("Sanya", 2, TimeUnit.SECONDS);
                    blockingQueue.offer("Savva", 2, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    out.println("catch in adder, exception " + e);
                    out.println("<hr>");
                    e.printStackTrace();
                }
            }
        }
        class Remover extends Thread {
            @Override
            public void run() {
                try {
                    blockingQueue.poll(2, TimeUnit.SECONDS);
                    blockingQueue.poll(2, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    out.println("catch in remover, exception " + e);
                    out.println("<hr>");
                    e.printStackTrace();
                }
            }
        }
        Adder adder = new Adder();
        Remover remover = new Remover();

        long time = System.currentTimeMillis();
        adder.start();
        try {
            adder.join();
            out.println("Adding time is " + (System.currentTimeMillis() - time));
            out.println("<hr>");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        out.println("blockingQueue contains ");
        blockingQueue.forEach(x -> out.println(x + " "));
        out.println("<hr>");

        remover.start();
        remover.interrupt();
        try {
            remover.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void workWithBlockingDeque(PrintWriter out, BlockingDeque blockingDeque) {
        class Adder extends Thread {
            @Override
            public void run() {
                try {
                    blockingDeque.offer("Savva");
                    blockingDeque.offer("Sasha", 2, TimeUnit.SECONDS);
                    blockingDeque.offerFirst("Sanya", 2, TimeUnit.SECONDS);
                    blockingDeque.offerLast("Syr", 2, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    out.println("catch in adder, exception " + e);
                    out.println("<hr>");
                    e.printStackTrace();
                }
            }
        }
        class Remover extends Thread {
            @Override
            public void run() {
                try {
                    blockingDeque.poll();
                    blockingDeque.poll(2, TimeUnit.SECONDS);
                    blockingDeque.pollFirst(2, TimeUnit.SECONDS);
                    blockingDeque.pollLast(2, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    out.println("catch in remover, exception " + e);
                    out.println("<hr>");
                    e.printStackTrace();
                }
            }
        }
        Adder adder = new Adder();
        Remover remover = new Remover();
        long time = System.currentTimeMillis();
        adder.start();
        try {
            adder.join();
            out.println("Adding time is " + (System.currentTimeMillis() - time));
            out.println("<hr>");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        out.println("blockingDeque contains ");
        blockingDeque.forEach(x -> {
            out.println(x + " ");
        });
        out.println("<hr>");

        remover.start();
        remover.interrupt();
        try {
            remover.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void workWithConcurrentSkipListSet(PrintWriter out, Set concurrentSkipListSet) {
        Queue<String> students = new ArrayDeque<>(5);
        students.addAll(Arrays.asList("Chabakauri", "Jurov", "Volkov", "Birya", "Kater"));
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            service.submit(() -> {
                String student = students.poll();
                concurrentSkipListSet.add(student);
            });
        }
        service.shutdown();
        while (!service.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void workWithcopyOnWriteArrayList(PrintWriter out, List copyOnWriteArrayList) {
        copyOnWriteArrayList.addAll(Arrays.asList("a", "b", "c"));
        for (Object item: copyOnWriteArrayList) {
            copyOnWriteArrayList.add("q");
        }
    }

    private void workWithcopyOnWriteArraySet(PrintWriter out, Set copyOnWriteArraySet) {
        copyOnWriteArraySet.addAll(Arrays.asList("a", "b", "c"));
        for (Object item: copyOnWriteArraySet) {
            if(!copyOnWriteArraySet.add("q")) {
                copyOnWriteArraySet.add("q "+copyOnWriteArraySet.size());
            }
        }
    }

    protected void workWithStreams(PrintWriter out) {
         /*List list = new ArrayList();
        for (int i = 0; i<4000; i++) {
            list.add(i);
        }
        long time = System.currentTimeMillis();
        list.stream().map(Object::toString).forEach(i -> out.println(i+" "));
        out.println("It took " + (System.currentTimeMillis() - time));
        out.println("<h2>-----------------------------------------------------------</h2>");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       long time = System.currentTimeMillis();
        list.parallelStream().map(Object::toString).forEach(i -> out.println(i+" "));
        out.println("It took " + (System.currentTimeMillis() - time));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        out.println("<h2>-----------------------------------------------------------</h2>");
        long time = System.currentTimeMillis();
        list.parallelStream().map(Object::toString).forEachOrdered(i -> out.println(i+" "));
        out.println("It took " + (System.currentTimeMillis() - time));*/

        List<Object> listSource = new LinkedList<>();
        Set<Object> setSource = new TreeSet();
        for(int i = 6; i>0; i--) {
            listSource.add(i);
            setSource.add(i);
        }
        out.println("list is ");
        listSource.forEach(i -> out.println(i));
        out.println("<hr>");
        out.println("set is ");
        setSource.forEach(i -> out.println(i));
        out.println("<hr>");

        Stream<Object> streamFromList = listSource.stream();
        Stream<Object> streamFromSet = setSource.stream();

        out.println("streamFromList is parallel - " + streamFromList.isParallel());
        out.println("<hr>");
        out.println("setSource is parallel - " + streamFromSet.isParallel());
        out.println("<hr>");

        printFromStreamFromList(listSource, out);
        out.println("<hr>");
        printFromStreamFromSet(setSource, out);

        out.println("<hr>");
        out.println("<hr>");
        out.println(Stream.of("w", "o", "l", "f").reduce((a, b) -> a + b));
        out.println("<hr>");
        out.println(Stream.of("w", "o", "l", "f").reduce("", (a, b) -> a + b));
        out.println("<hr>");
        out.println(Stream.of("w", "o", "l", "f").reduce("hello ", (a, b) -> a + b));
        out.println("<hr>");
        Stream<String> emptyStream = Stream.empty();
        out.println("before empty stream 1 ");
        out.println(emptyStream.reduce((a, b) -> a + b));
        out.println(" after empty stream 1");
        out.println("<hr>");
        out.println("before empty stream 2 ");
        emptyStream = Stream.empty();
        out.println(emptyStream.reduce("empty", (a, b) -> a + b));
        out.println(" after empty stream 2");
        out.println("<hr>");
        BinaryOperator<Integer> bo = (a, b) -> a*b;
        out.println(Stream.of(3, 4, 5).reduce(0, (a, b) -> a+b, bo));

        out.println("<hr>");
        out.println("<hr>");
        out.println(Stream.of("w", "o", "l", "f").collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString());
        out.println("<hr>");
        Set setCollection = Stream.of("w", "o", "l", "f").collect(Collectors.toCollection(HashSet::new));
        setCollection.forEach(out::println);
        out.println("<hr>");
        List listCollection = Stream.of("w", "o", "l", "f").collect(Collectors.toCollection(ArrayList::new));
        listCollection.forEach(out::println);


    }

    private void printFromStreamFromList(List<Object> listSource, PrintWriter out) {
        out.println("print values in stream from list");
        listSource.stream().forEach(i -> out.println(i + " "));
        out.println("<hr>");
        out.println("print values in parallel stream from list");
        listSource.parallelStream().forEach(i -> out.println(i + " "));
        out.println("<hr>");

        out.println("OPERATION findAny");
        out.println("<hr>");

        out.println("findAny stream from list");
        listSource.stream().findAny().ifPresent(out::println);
        out.println("<hr>");
        out.println("findAny parallel stream from list");
        listSource.parallelStream().findAny().ifPresent(out::println);//unpredictable result?
        out.println("<hr>");
        out.println("findAny not parallel unordered stream from list");
        listSource.stream().unordered().findAny().ifPresent(out::println);//doesn't make sense
        out.println("<hr>");
        out.println("findAny parallel stream, unordered after parallel from list");
        listSource.stream().parallel().unordered().findAny().ifPresent(out::println);
        out.println("<hr>");
        out.println("findAny parallel stream, unordered before parallel from list");
        listSource.stream().unordered().parallel().findAny().ifPresent(out::println);
        out.println("<hr>");

        out.println("OPERATION findFirst");
        out.println("<hr>");

        out.println("findFirst not parallel stream from list");
        listSource.stream().findFirst().ifPresent(out::println);
        out.println("<hr>");
        out.println("findFirst parallel stream from list");
        listSource.parallelStream().findFirst().ifPresent(out::println);
        out.println("<hr>");
        out.println("findFirst not parallel unordered stream from list");
        listSource.stream().unordered().findFirst().ifPresent(out::println);//doesn't make sense
        out.println("<hr>");
        out.println("findFirst parallel stream, unordered after parallel from list");
        listSource.stream().parallel().unordered().findFirst().ifPresent(out::println);
        out.println("<hr>");
        out.println("findFirst parallel stream, unordered before parallel from list");
        listSource.stream().unordered().parallel().findFirst().ifPresent(out::println);
        out.println("<hr>");

        out.println("OPERATION skip");
        out.println("<hr>");

        out.println("skip 3 not parallel stream from list");
        listSource.stream().skip(3).forEach(out::println);
        out.println("<hr>");
        out.println("skip 3 parallel stream from list");
        listSource.parallelStream().skip(3).forEach(out::println);
        out.println("<hr>");
        out.println("skip 3 not parallel unordered stream from list");
        listSource.stream().unordered().skip(3).forEach(out::println);//doesn't make sense
        out.println("<hr>");
        out.println("skip 3 parallel stream, unordered after parallel from list");
        listSource.stream().parallel().unordered().skip(3).forEach(out::println);
        out.println("<hr>");
        out.println("skip 3 parallel stream, unordered before parallel from list");
        listSource.stream().unordered().parallel().skip(3).forEach(out::println);
        out.println("<hr>");

        out.println("OPERATION limit");
        out.println("<hr>");

        out.println("limit 2 not parallel stream from list");
        listSource.stream().limit(2).forEach(out::println);
        out.println("<hr>");
        out.println("limit 2 parallel stream from list");
        listSource.parallelStream().limit(2).forEach(out::println);
        out.println("<hr>");
        out.println("limit 2 not parallel unordered stream from list");
        listSource.stream().unordered().limit(2).forEach(out::println);//doesn't make sense
        out.println("<hr>");
        out.println("limit 2 parallel stream, unordered after parallel from list");
        listSource.stream().parallel().unordered().limit(2).forEach(out::println);
        out.println("<hr>");
        out.println("limit 2 parallel stream, unordered before parallel from list");
        listSource.stream().unordered().parallel().limit(2).forEach(out::println);
        out.println("<hr>");

        out.println("OPERATIONS skip-limit-findFirst");
        out.println("<hr>");

        out.println("skip 2, limit 2, findFirst not parallel stream from list");
        listSource.stream().skip(2).limit(2).findFirst().ifPresent(out::println);
        out.println("<hr>");
        out.println("skip 2, limit 2, findFirst parallel stream from list");
        listSource.parallelStream().skip(2).limit(2).findFirst().ifPresent(out::println);
        out.println("<hr>");
        out.println("skip 2, limit 2, findFirst not parallel unordered stream from list");
        listSource.stream().skip(2).limit(2).findFirst().ifPresent(out::println);//doesn't make sense
        out.println("<hr>");
        out.println("skip 2, limit 2, findFirst parallel stream, unordered after parallel from list");
        listSource.stream().skip(2).limit(2).findFirst().ifPresent(out::println);
        out.println("<hr>");
        out.println("skip 2, limit 2, findFirst parallel stream, unordered before parallel from list");
        listSource.stream().skip(2).limit(2).findFirst().ifPresent(out::println);
        out.println("<hr>");

        out.println("OPERATION map * 2");
        out.println("<hr>");

        List<Integer> integerList = new ArrayList<>();
        listSource.forEach(x -> {if(x instanceof Integer) {
            integerList.add((Integer) x);
        }
        });
        out.println("map * 2 not parallel stream from list");
        integerList.stream().map(x -> x*2).forEach(i -> out.println(i));
        out.println("<hr>");
        out.println("map * 2 parallel stream from list");
        integerList.parallelStream().map(x -> x*2).forEach(i -> out.println(i));
        out.println("<hr>");
        out.println("map * 2 not parallel unordered stream from list");//doesn't make sense
        integerList.stream().unordered().map(x -> x*2).forEach(i -> out.println(i));//???
        out.println("<hr>");
        out.println("map * 2 parallel stream, unordered after parallel from list");
        integerList.stream().parallel().unordered().map(x -> x*2).forEach(i -> out.println(i));//???
        out.println("<hr>");
        out.println("map * 2 parallel stream, unordered before parallel from list");
        integerList.stream().unordered().parallel().map(x -> x*2).forEach(i -> out.println(i));//???
        out.println("<hr>");

        out.println("OPERATION reduce");
        out.println("<hr>");

        out.println("reduce not parallel stream from list");
        out.println(integerList.stream().reduce(0,(c,s1)->c + s1, (s2,s3)->s2+s3));
        out.println("<hr>");
        out.println("reduce parallel stream from list");
        out.println(integerList.parallelStream().reduce(0,(c,s1)->c + s1, (s2,s3)->s2+s3));
        out.println("<hr>");
        out.println("reduce not parallel unordered stream from list");//doesn't make sense
        out.println(integerList.stream().unordered().reduce(0,(c,s1)->c + s1, (s2,s3)->s2+s3));
        out.println("<hr>");
        out.println("reduce parallel stream, unordered after parallel from list");
        out.println(integerList.stream().parallel().unordered().reduce(0,(c,s1)->c + s1, (s2,s3)->s2+s3));
        out.println("<hr>");
        out.println("reduce parallel stream, unordered before parallel from list");
        out.println(integerList.stream().unordered().parallel().reduce(0,(c,s1)->c + s1, (s2,s3)->s2+s3));
        out.println("<hr>");

        out.println("OPERATION reduce BREAKING RULES");
        out.println("<hr>");

        out.println("reduce not parallel stream, breaking rule from list");
        out.println(integerList.stream().reduce(0,(a,b)->a - b));
        out.println("<hr>");
        out.println("reduce not parallel stream, accumulator and combiner, breaking rule for combiner, from list");
        out.println(integerList.stream().reduce(0,(a,b)->a + b, (a,b)->a - b));
        out.println("<hr>");
        out.println("reduce parallel stream, only accumulator, breaking rule from list");
        out.println(integerList.parallelStream().reduce(0,(a,b)->a - b));
        out.println("<hr>");
        out.println("reduce parallel stream, accumulator and combiner, breaking rule for both, from list");
        out.println(integerList.parallelStream().reduce(0,(a,b)->a - b, (a,b)->a - b));
        out.println("<hr>");
        out.println("reduce parallel stream, accumulator and combiner, breaking rule for combiner, from list");
        out.println(integerList.parallelStream().reduce(0,(a,b)->a + b, (a,b)->a - b));
        out.println("<hr>");
        //collect?
    }

    private void printFromStreamFromSet(Set<Object> setSource, PrintWriter out) {
        out.println("print values in stream from set");
        setSource.stream().forEach(i -> out.println(i + " "));
        out.println("<hr>");
        out.println("print values in parallel stream from set");
        setSource.parallelStream().forEach(i -> out.println(i + " "));
        out.println("<hr>");

        out.println("OPERATION findAny");
        out.println("<hr>");

        out.println("findAny not parallel stream from set");
        setSource.stream().findAny().ifPresent(out::println);
        out.println("<hr>");
        out.println("findAny parallel stream from set");
        setSource.parallelStream().findAny().ifPresent(out::println);//unpredictable result?
        out.println("<hr>");
        out.println("findAny not parallel unordered stream from set");
        setSource.stream().unordered().findAny().ifPresent(out::println);
        out.println("<hr>");
        out.println("findAny parallel stream, unordered after parallel from set");
        setSource.stream().parallel().unordered().findAny().ifPresent(out::println);
        out.println("<hr>");
        out.println("findAny parallel stream, unordered before parallel from set");
        setSource.stream().unordered().parallel().findAny().ifPresent(out::println);
        out.println("<hr>");

        out.println("OPERATION findFirst");
        out.println("<hr>");

        out.println("findFirst not parallel stream from set");
        setSource.stream().findFirst().ifPresent(out::println);
        out.println("<hr>");
        out.println("findFirst parallel stream from set");
        setSource.parallelStream().findFirst().ifPresent(out::println);
        out.println("<hr>");
        out.println("findFirst not parallel unordered stream from set");
        setSource.stream().unordered().findFirst().ifPresent(out::println);
        out.println("<hr>");
        out.println("findFirst parallel stream, unordered after parallel from set");
        setSource.stream().parallel().unordered().findFirst().ifPresent(out::println);
        out.println("<hr>");
        out.println("findFirst parallel stream, unordered before parallel from set");
        setSource.stream().unordered().parallel().findFirst().ifPresent(out::println);
        out.println("<hr>");

        out.println("OPERATION skip");
        out.println("<hr>");

        out.println("skip 3 not parallel stream from set");
        setSource.stream().skip(3).forEach(out::println);
        out.println("<hr>");
        out.println("skip 3 parallel stream from set");
        setSource.parallelStream().skip(3).forEach(out::println);
        out.println("<hr>");
        out.println("skip 3 not parallel unordered stream set");
        setSource.stream().unordered().skip(3).forEach(out::println);//doesn't make sense
        out.println("<hr>");
        out.println("skip 3 parallel stream, unordered after parallel from set");
        setSource.stream().parallel().unordered().skip(3).forEach(out::println);
        out.println("<hr>");
        out.println("skip 3 parallel stream, unordered before parallel from set");
        setSource.stream().unordered().parallel().skip(3).forEach(out::println);
        out.println("<hr>");

        out.println("OPERATION limit");
        out.println("<hr>");

        out.println("limit 2 not parallel stream from set");
        setSource.stream().limit(2).forEach(out::println);
        out.println("<hr>");
        out.println("limit 2 parallel stream from set");
        setSource.parallelStream().limit(2).forEach(out::println);
        out.println("<hr>");
        out.println("limit 2 not parallel unordered stream from set");
        setSource.stream().unordered().limit(2).forEach(out::println);//doesn't make sense
        out.println("<hr>");
        out.println("limit 2 parallel stream, unordered after parallel from set");
        setSource.stream().parallel().unordered().limit(2).forEach(out::println);
        out.println("<hr>");
        out.println("limit 2 parallel stream, unordered before parallel from set");
        setSource.stream().unordered().parallel().limit(2).forEach(out::println);
        out.println("<hr>");

        out.println("OPERATIONS skip-limit-findFirst");
        out.println("<hr>");

        out.println("skip 2, limit 2, findFirst not parallel stream from set");
        setSource.stream().skip(2).limit(2).findFirst().ifPresent(out::println);
        out.println("<hr>");
        out.println("skip 2, limit 2, findFirst parallel stream from set");
        setSource.parallelStream().skip(2).limit(2).findFirst().ifPresent(out::println);
        out.println("<hr>");
        out.println("skip 2, limit 2, findFirst not parallel unordered stream from set");
        setSource.stream().skip(2).limit(2).findFirst().ifPresent(out::println);//doesn't make sense
        out.println("<hr>");
        out.println("skip 2, limit 2, findFirst parallel stream, unordered after parallel from set");
        setSource.parallelStream().unordered().skip(2).limit(2).findFirst().ifPresent(out::println);
        out.println("<hr>");
        out.println("skip 2, limit 2, findFirst parallel stream, unordered before parallel from set");
        setSource.stream().unordered().parallel().skip(2).limit(2).findFirst().ifPresent(out::println);
        out.println("<hr>");

        out.println("OPERATION map * 2");
        out.println("<hr>");

        List<Integer> integerSet = new ArrayList<>();
        setSource.forEach(x -> {if(x instanceof Integer) {
            integerSet.add((Integer) x);
        }
        });
        out.println("map * 2 not parallel stream from set");
        integerSet.stream().map(x -> x*2).forEach(i -> out.println(i));
        out.println("<hr>");
        out.println("map * 2 parallel stream from set");
        integerSet.parallelStream().map(x -> x*2).forEach(i -> out.println(i));
        out.println("<hr>");
        out.println("map * 2 not parallel unordered stream from set");//doesn't make sense
        integerSet.stream().unordered().map(x -> x*2).forEach(i -> out.println(i));//???
        out.println("<hr>");
        out.println("map * 2 parallel stream, unordered after parallel from set");
        integerSet.stream().parallel().unordered().map(x -> x*2).forEach(i -> out.println(i));//???
        out.println("<hr>");
        out.println("map * 2 parallel stream, unordered before parallel from set");
        integerSet.stream().unordered().parallel().map(x -> x*2).forEach(i -> out.println(i));//???
        out.println("<hr>");

        out.println("OPERATION reduce");
        out.println("<hr>");

        out.println("reduce not parallel stream from set");
        out.println(integerSet.stream().reduce("",(c,s1)->c + s1, (s2,s3)->s2+s3));
        out.println("<hr>");
        out.println("reduce parallel stream from set");
        out.println(integerSet.parallelStream().reduce("",(c,s1)->c + s1, (s2,s3)->s2+s3));
        out.println("<hr>");
        out.println("reduce not parallel unordered stream from set");//doesn't make sense
        out.println(integerSet.stream().unordered().reduce("",(c,s1)->c + s1, (s2,s3)->s2+s3));
        out.println("<hr>");
        out.println("reduce parallel stream, unordered after parallel from set");
        out.println(integerSet.stream().parallel().unordered().reduce("",(c,s1)->c + s1, (s2,s3)->s2+s3));
        out.println("<hr>");
        out.println("reduce parallel stream, unordered before parallel from set");
        out.println(integerSet.stream().unordered().parallel().reduce("",(c,s1)->c + s1, (s2,s3)->s2+s3));
        out.println("<hr>");

        out.println("OPERATION reduce BREAKING RULES");
        out.println("<hr>");

        out.println("reduce not parallel stream, breaking rule from set");
        out.println(integerSet.stream().reduce(0,(a,b)->a - b));
        out.println("<hr>");
        out.println("reduce not parallel stream, accumulator and combiner, breaking rule for combiner, from set");
        out.println(integerSet.stream().reduce(0,(a,b)->a + b, (a,b)->a - b));
        out.println("<hr>");
        out.println("reduce parallel stream, only accumulator, breaking rule from set");
        out.println(integerSet.parallelStream().reduce(0,(a,b)->a - b));
        out.println("<hr>");
        out.println("reduce parallel stream, accumulator and combiner, breaking rule for both, from set");
        out.println(integerSet.parallelStream().reduce(0,(a,b)->a - b, (a,b)->a - b));
        out.println("<hr>");
        out.println("reduce parallel stream, accumulator and combiner, breaking rule for combiner, from set");
        out.println(integerSet.parallelStream().reduce(0,(a,b)->a + b, (a,b)->a - b));
        out.println("<hr>");
        //collect?
    }

    protected void workWithCyclicBareer(PrintWriter out) {
        CyclicBarrier barrier = new CyclicBarrier(5);
        CyclicBarrier barrier2 = new CyclicBarrier(5);
        ExecutorService service = Executors.newFixedThreadPool(15);
        for (int i = 0; i < 15; i++) {
            int tempVar = i;
            service.submit(() -> {
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                synchronized (out) {
                    out.println("This is service number " + tempVar + " before await");
                    out.println("<h2>-----------------------------------------------------------</h2>");
                }
                try {
                    barrier2.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                synchronized (out) {
                    out.println("This is service number " + tempVar + " after await");
                    out.println("<h2>-----------------------------------------------------------</h2>");
                }
            });
        }
        service.shutdown();
        while (!service.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void workWithForkJoinPool(PrintWriter out) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        int[] animalWeight = new int[10];
        forkJoinPool.invoke(new WeightAnimals(out, animalWeight, 0, animalWeight.length));
        for(int i = 0; i < animalWeight.length; i++) {
            out.println("<h2>animalWeight " + i + " " + animalWeight[i] + "</h2>");
        }
        out.println("<hr>");
    }

    protected void workWithForkJoinTask(PrintWriter out) {
        final int AMOUNT_OF_ANIMALS = 10;
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        int result = forkJoinPool.invoke(new CountSumWeightAnimals(out, 0, AMOUNT_OF_ANIMALS));
        out.println();
        out.println("<h2>final result is " + result + "</h2>");
        out.println("<hr>");
    }

    protected void workWithSemaphore(PrintWriter out) {
        final int NUMBER_OF_THREADS = 100;
        Semaphore semaphore = new Semaphore(10, true);

        class MyThread extends Thread {
            private final int THREAD_NUMBER;

            public MyThread(int threadNumber) {
                this.THREAD_NUMBER = threadNumber;
            }

            @Override
            public void run() {
                out.println("This is thread number " + THREAD_NUMBER +
                        ". availablePermits are " + semaphore.availablePermits() + " drainPermits are " + semaphore.drainPermits() +
                        ". hasQueuedThreads is " + semaphore.hasQueuedThreads());
                out.println("<hr>");
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                out.println("This is thread number " + THREAD_NUMBER);
                out.println("<hr>");
            }
        }

        MyThread[] myThreads = new MyThread[NUMBER_OF_THREADS];
        for (int i = 0; i < myThreads.length; i++) {
            myThreads[i] = new MyThread(i);
        }

        for (int i = 0; i < myThreads.length; i++) {
            if(semaphore.hasQueuedThreads()) {
                out.println("semaphore.hasQueuedThreads() is true, i = " + i);
                semaphore.release();
            }
            myThreads[i].start();
        }
    }

    protected void workWithCountDownLatch(PrintWriter out) {

    }

    protected void workWithCyclicBarrier(PrintWriter out) {

    }

    protected void workWithPhaser(PrintWriter out) {

    }
}
