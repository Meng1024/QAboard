package com.QAboard;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class MyThread extends Thread {
    private int tid;
    
    public MyThread(int tid) {
        this.tid = tid;
    }
    
    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }
    @Override 
    public void run() {
        try {
            for(int i = 0; i < 10; ++i) {
                Thread.sleep(1000);
                System.out.println(String.format("%d, %d", tid, i));
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}


class Consumer implements Runnable {
    private BlockingQueue<String> q;
    public Consumer(BlockingQueue<String> q) {
        this.q = q;
    }
    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":" + q.take());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}


class Producer implements Runnable {
    private BlockingQueue<String> q;
    public Producer(BlockingQueue<String> q) {
        this.q = q;
    }
    @Override
    public void run() {
        try {
            for(int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                q.put(String.valueOf(i));
                System.out.println("produce");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}

public class MultiThreadTest {
    // two ways test Thread
    public static void testThread() {
//        for(int i = 0; i < 10; i++) {
//            new MyThread(i).start();
//        }
//        
        for(int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override 
                public void run() {
                    try {
                        for(int i = 0; i < 10; ++i) {
                            Thread.sleep(1000);
                            System.out.println(String.format("T2 : %d", i));
                        }
                        
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    
    // test snchronized
    private static Object obj = new Object();
    public static void testSynchronized1() {
        synchronized(new Object()) {
            try {
                for(int i = 0; i < 10; i++) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T4 %d", i));
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    public static void testSynchronized2() {
        synchronized(obj) {
            try {
                for(int i = 0; i < 10; i++) {
                    Thread.sleep(1000);
                    System.out.println("T5 " + i);
                }
            } catch(Exception e) {
                e.printStackTrace();            
            }
        }
    }
    
    public static void testSynchronized() {
        for(int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0; i < 10; i++) {
                        testSynchronized1();
                        testSynchronized2();
                    }
                }
            }).start();
        }
    }
    
    //test BlockingQueue
    
    public static void testBlockingQueue() {
        BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q), "consumer1").start();
        new Thread(new Consumer(q), "consumer2").start();
    }
    
    //test thread local 
    public static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    public static int UserId;
    
    
    public static void testThreadLocal() {
        for(int i = 0; i < 10; i++) {
            final int finalI  = i;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        threadLocalUserIds.set(finalI);
                        Thread.sleep(1000);
                        System.out.println("ThreadLocalId is " + threadLocalUserIds.get());
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    
                }
                
            }).start();
        }
        
        for (int i = 0; i < 10; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        UserId = finalI;
                        Thread.sleep(1000);
                        System.out.println("UserId:" + UserId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    
    //test Executor
    public static void testExecutor() {
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Runnable(){

            @Override
            public void run() {
                for(int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Executor1:" + i);
                    } catch(Exception e) {
                        
                    }
                }
            }
            
        });
        service.submit(new Runnable(){

            @Override
            public void run() {
                for(int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Executor2:" + i);
                    } catch(Exception e) {
                        
                    }
                }
            }
            
        });
    }
    
    // test Atomic
    
    private static int counter = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void testWithoutAtomic() {
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        for (int j = 0; j < 10; ++j) {
                            counter++;
                            System.out.println(counter);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    public static void testWithAtomic() {
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        for (int j = 0; j < 10; ++j) {
                            System.out.println(atomicInteger.incrementAndGet());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    public static void main(String[] args) {
        //testThread();
        //testSynchronized();
        //testBlockingQueue();
        //testThreadLocal();
        //testExecutor();
        //testWithoutAtomic();
        testWithAtomic();
    }
}
