package com.for_comprehension.reactor.l2_thread_pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class L2_ThreadPoolExecutor {

    record Example1() {
        public static void main(String[] args) {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            executor.submit(() -> {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Hello from " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("e = " + e);
                    }
                }
            });

            executor.shutdown();
        }
    }

    record Example2() {
        public static void main(String[] args) {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            executor.submit(() -> {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Hello from " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000000);
                    } catch (InterruptedException e) {
                        System.out.println("e = " + e);
                    }
                }
            });

            // stops and waits for the currently executing tasks to finish
            executor.shutdown();
        }
    }

    record Example3() {
        public static void main(String[] args) throws InterruptedException {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            executor.submit(() -> {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Hello from " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000000);
                    } catch (InterruptedException e) {
                        System.out.println("e = " + e);
                        break;
                    }
                }
            });

            Thread.sleep(2000);
            executor.shutdownNow();
        }
    }

    record Example4() {
        public static void main(String[] args) throws InterruptedException {
            ExecutorService executor = Executors.newFixedThreadPool(4, r -> {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            });
            executor.submit(() -> {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Hello from " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(1_000);
                    } catch (InterruptedException e) {
                        System.out.println("e = " + e);
                        break;
                    }
                }
            });

            // initiates shutdown, but does not block
            executor.shutdown();
            // waits for the currently executing tasks to finish
            executor.awaitTermination(1, TimeUnit.MINUTES);
        }
    }

    record Example5() {
        public static void main(String[] args) throws InterruptedException {
            ExecutorService executor = Executors.newFixedThreadPool(4, r -> {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            });
            executor.submit(() -> {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Hello from " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(1_000);
                    } catch (InterruptedException e) {
                        System.out.println("e = " + e);
                        break;
                    }
                }
            });

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    record Example6() {
        public static void main(String[] args) throws InterruptedException {
            try (var e = Executors.newFixedThreadPool(4, r -> {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            })) {
                e.submit(() -> {
                    for (int i = 0; i < 10; i++) {
                        System.out.println("Hello from " + Thread.currentThread().getName());
                        try {
                            Thread.sleep(1_000);
                        } catch (InterruptedException ex) {
                            System.out.println("ex = " + ex);
                            break;
                        }
                    }
                });
            }
        }
    }

    record Example400() {
        public static void main(String[] args) {
            Connection connection = null;

            try {
                connection.prepareStatement("SELECT * FROM users");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
