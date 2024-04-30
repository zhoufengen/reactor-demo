package com.example.reactordemo.controller;

import com.example.reactordemo.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhoufe
 * @date 2024/4/25 16:20
 */
@RestController
@RequestMapping("/mono-flux")
public class MonoFluxController {

    /** Mono
     * 表示的是包含 0 到 1 个元素的异步序列。在该序列中可以包含三种不同类型的消息通知：
     * 正常的包含元素的消息
     * 序列结束的消息
     * 序列出错的消息
     * @return
     */
    @GetMapping("/getMono")
    public Mono<R> getMono() {
        return Mono.just(R.ok().message("成功12321"));
    }

    /** Flux
     * 表示的是包含 0 到 N 个元素的异步序列。在该序列中可以包含三种不同类型的消息通知：
     * 正常的包含元素的消息
     * 序列结束的消息
     * 序列出错的消息
     * @return
     */
    @GetMapping("/getFlux")
    public Flux<R> getFlux() {
        return Flux.just(R.ok().message("成功1"), R.ok().message("成功2"), R.ok().message("成功3"));
    }

    @GetMapping("/getZipWith")
    public Flux<R> getZipWith() {
        return Flux.just(R.ok().message("成功1"), R.ok().message("成功2")).zipWith(Flux.just(R.ok().message("成功3"), R.ok().message("成功4")))
                .map(t -> R.ok().message(t.getT1().getMessage() + t.getT2().getMessage()));
    }


    public static void main(String[] args) {

        //#################2 Mono & Flux###########

//        使用静态工厂类创建Flux
        ////可以指定序列中包含的全部元素。创建出来的 Flux 序列在发布这些元素之后会自动结束。
//        Flux.just("Hello", "World").subscribe(System.out::println);
        ////可以从一个数组、Iterable 对象或 Stream 对象中创建 Flux 对象。
//        Flux.fromArray(new Integer[] {1, 2, 3}).subscribe(System.out::println);
        ////创建一个不包含任何元素，只发布结束消息的序列。
//        Flux.empty().subscribe(System.out::println);
        ////创建包含从 start 起始的 count 个数量的 Integer 对象的序列。
//        Flux.range(1, 10).subscribe(System.out::println);
        ////创建一个包含了从 0 开始递增的 Long 对象的序列。其中包含的元素按照指定的间隔来发布。除了间隔时间之外，还可以指定起始元素发布之前的延迟时间。
//        Flux.interval(Duration.of(0, ChronoUnit.SECONDS), Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);
        //要保持不退出进程
//        while (true) {
//            try {
//                Thread.sleep(60*1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        //除了上述的方式之外，还可以使用 generate()、create()方法来自定义流数据的产生过程：
        //generate()

        //generate 只提供序列中单个消息的产生逻辑(同步通知)，其中的 sink.next()最多只能调用一次，比如上面的代码中，产生一个Echo消息后就结束了。
//        Flux.generate(sink -> {
//            sink.next("Echo1");
//            sink.complete();
//        }).subscribe(System.out::println);

        //generate用别的方法也可以调用多个next方法，生成多个消息
//        Flux.generate(
//                //初始化状态
//                () -> 0,
//                //生成数据
//                (state, sink) -> {
//                    sink.next("3 x " + state + " = " + 3 * state);
//                    if (state == 10) {
//                        sink.complete();
//                    }
//                    return state + 1;
//                }
//        ).subscribe(System.out::println);

        //也可以使用create()调用多个next方法，生成多个消息
        //create 提供的是整个序列的产生逻辑，sink.next()可以调用多次(异步通知)
//        Flux.create(sink -> {
//            for (int i = 0; i < 10; i++) {
//                sink.next("Echo" + i);
//            }
//            sink.complete();
//        }).subscribe(System.out::println);


        //使用静态工厂类创建Mono
        //Mono 的创建方式与 Flux 是很相似的。 除了Flux 所拥有的构造方式之外，还可以支持与Callable、Runnable、Supplier 等接口集成。
//        Mono.fromCallable(() -> "Hello callable").subscribe(System.out::println);
//        Mono.fromRunnable(() -> System.out.println("Hello runnable")).subscribe();
//        Mono.fromSupplier(() -> "Hello supplier").subscribe(System.out::println);
//
//        Mono.fromFuture(CompletableFuture.supplyAsync(() -> "Hello CompletableFuture")).subscribe(System.out::println);
//
//        Mono.fromSupplier(() -> "Mono1").subscribe(System.out::println);
//        Mono.justOrEmpty(Optional.of("Mono2")).subscribe(System.out::println);
//        Mono.create(sink -> sink.success("Mono3")).subscribe(System.out::println);
//



        //#################3 流计算###########
        //3.1缓冲
        //buffer 是流处理中非常常用的一种处理，意思就是将流的一段截停后再做处理。
//        Flux.range(1, 10)
//                .buffer(3)
//                .subscribe(System.out::println);

        //第一个buffer(20)是指凑足20个数字后再进行处理，该语句会输出5组数据(按20分组)
//        Flux.range(1, 100).buffer(20).subscribe(System.out::println);
        //第二个buffer(5)是指每5秒进行一次buffer操作，该语句会输出2组数据(按5秒分组)
//        Flux.interval(Duration.of(0, ChronoUnit.SECONDS),
//                        Duration.of(1, ChronoUnit.SECONDS))
//                .buffer(Duration.of(5, ChronoUnit.SECONDS)).
//                take(2).toStream().forEach(System.out::println);
//
//        try {
//            Thread.sleep(60*1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        //bufferUtil(Predicate p)是指等到某个元素满足断言(条件)时进行收集处理，这里将会输出[1,2],[3,4]..这样的奇偶数字对
//        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
        //bufferWhile(Predicate p)则仅仅是收集满足断言(条件)的元素，这里将会输出2,4,6..这样的偶数
//        Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);
        //window函数，后者的不同在于其在缓冲截停后并不会输出一些元素列表，而是直接转换为Flux对象
//        Flux.range(1, 100).window(20)
//                .subscribe(flux ->
//                        flux.buffer(5).subscribe(System.out::println));

        //3.2）过滤提取
       // Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);
        //take 函数可以用来提取想要的元素
//        第一个take(2)指提取前面的两个元素；
//        第二个takeLast(2)指提取最后的两个元素；
//        第三个takeWhile(Predicate p)指提取满足条件的元素，这里是1-4
//        第四个takeUtil(Predicate p)指一直提取直到满足条件的元素出现为止，这里是1-6
        //Flux.range(1, 10).take(2).subscribe(System.out::println);
        //Flux.range(1, 10).takeLast(2).subscribe(System.out::println);
        //Flux.range(1, 10).takeWhile(i -> i < 5).subscribe(System.out::println);
        //Flux.range(1, 10).takeUntil(i -> i == 6).subscribe(System.out::println);

        //3.3）转换
        //map函数可以对元素进行转换，比如将1-10的数字转换为字符串
//        Flux.range(1, 10).map(i -> i + "dd")
//                .subscribe(System.out::println);
        //flatMap函数可以对元素进行转换，比如将1-10的数字转换为字符串
//        Flux.range(1, 10).flatMap(i -> Mono.just(i + "toString"))
//                .subscribe(System.out::println);
        //concatMap函数可以对元素进行转换，比如将1-10的数字转换为字符串：顺序处理
//        Flux.range(1, 10).concatMap(i -> Mono.just(i + "serialnumber"))
//                .subscribe(System.out::println);

        //3.4)合并不是合流
        //对两个流中的元素进行合并处理，这与合并两个数组有点相似，但结合流的特点又会有不同的需求。
        //使用zipWith函数可以实现简单的流元素合并处理：zipWith操作符会将两个流中相同索引位置的元素组合成一对，然后发出这些对。
//        Flux.just("I1", "You2")
//                .zipWith(Flux.just("Win3", "Lose4"))
//                .subscribe(System.out::println);

//
//        Flux.just("I1", "You3")
//                .zipWith(Flux.just("Win2", "Lose4r"),
//                        (s1, s2) -> String.format("%s!%s!", s1, s2))
//                .subscribe(System.out::println);
//
//        注意到zipWith是分别按照元素在流中的顺序进行两两合并的，合并后的流长度则最短的流为准，遵循最短对齐原则。
//        用于实现合并的还有 combineLastest函数，combinLastest 会动态的将流中新产生元素(末位)进行合并，注意是只要产生新元素都会触发合并动作并产生一个结果元素，如下面的代码：

//        Flux.combineLatest(
//                Arrays::toString,
//                Flux.interval(Duration.of(0, ChronoUnit.MILLIS),
//                        Duration.of(100, ChronoUnit.MILLIS)).take(2),
//                Flux.interval(Duration.of(50, ChronoUnit.MILLIS),
//                        Duration.of(100, ChronoUnit.MILLIS)).take(2)
//        ).toStream().forEach(System.out::println);

//        Flux<String> source1 = Flux.just("I", "Love", "Reactive", "Programming");
//        Flux<String> source2 = Flux.just("I", "Hate", "Reactive", "Misconfigurations");
//        Flux.combineLatest(Arrays::toString, source1, source2).toStream().forEach(System.out::println);

        //合流
//        Flux.merge(Flux.interval(
//                                Duration.of(0, ChronoUnit.MILLIS),
//                                Duration.of(100, ChronoUnit.MILLIS)).take(2),
//                        Flux.interval(
//                                Duration.of(50, ChronoUnit.MILLIS),
//                                Duration.of(100, ChronoUnit.MILLIS)).take(2))
//                .toStream()
//                .forEach(System.out::println);
//        System.out.println("---");
//        Flux.mergeSequential(Flux.interval(
//                                Duration.of(0, ChronoUnit.MILLIS),
//                                Duration.of(100, ChronoUnit.MILLIS)).take(2),
//                        Flux.interval(
//                                Duration.of(50, ChronoUnit.MILLIS),
//                                Duration.of(100, ChronoUnit.MILLIS)).take(2))
//                .toStream()
//                .forEach(System.out::println);


//        Flux.just(1, 2)
//                .flatMap(x -> Flux.interval(Duration.of(x * 10, ChronoUnit.MILLIS),
//                        Duration.of(10, ChronoUnit.MILLIS)).take(x))
//                .toStream()
//                .forEach(System.out::println);




        //3.5）积累
        // reduce 和 reduceWith 操作符对流中包含的所有元素进行累积操作，得到一个包含计算结果的 Mono 序列。累积操作是通过一个 BiFunction 来表示的。reduceWith 允许在在操作时指定一个起始值(与第一个元素进行运算)
        //Flux.range(1, 100).reduce((x, y) -> x + y).subscribe(System.out::println);
        //从100开始累加
//        Flux.range(1, 100).reduceWith(() -> 100, (x, y) -> x + y).subscribe(System.out::println);

        //4.）异常错误
        //错误发生时中断并输出错误：
//        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
//                .map(i -> {
//                    if (i == 5) {
//                        throw new RuntimeException("error!!!");
//                    }
//                    return i;
//                })
//                .doOnError(e -> System.out.println("error:" + e.getMessage()))
//                .subscribe(System.out::println);

        //将正常消息和错误消息分别打印
        //正常和异常的消息同一个流发出返回
//        Flux.just(1, 2)
//                .concatWith(Mono.error(new IllegalStateException()))
//                .subscribe(System.out::println, System.err::println);


        //当产生错误时默认返回0
//        Flux.just(1, 2)
//                .concatWith(Mono.error(new IllegalStateException()))
//                .onErrorReturn(0)
//                .subscribe(System.out::println);


        //自定义异常时的处理
//        Flux.just(1, 2)
//                .concatWith(Mono.error(new IllegalArgumentException()))
//                .onErrorResume(e -> {
//                    if (e instanceof IllegalStateException) {
//                        return Mono.just(0);
//                    } else if (e instanceof IllegalArgumentException) {
//                        return Mono.just(-1);
//                    }
//                    return Mono.empty();
//                })
//                .subscribe(System.out::println);

       //当产生错误时重试：流从头发射1次
//        Flux.just(1, 2)
//                .concatWith(Mono.error(new IllegalStateException()))
//                .retry(1)
//                .subscribe(System.out::println);


        //5.）线程调度
        //响应式是异步化的，那么就会涉及到多线程的调度
        //Reactor 提供了非常方便的调度器(Schedulers)工具方法，可以指定流的产生以及转换(计算)发布所采用的线程调度方式。
//        immediate	采用当前线程
//        single	单一可复用的线程
//        elastic	弹性可复用的线程池(IO型)
//                parallel	并行操作优化的线程池(CPU计算型)
//        timer	支持任务调度的线程池
//        fromExecutorService	自定义线程池

//        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
//                .map(i -> {
//                    System.out.println("map:" + Thread.currentThread().getName());
//                    return i;
//                })
//                .subscribeOn(Schedulers.newSingle("subscribeOn"))
//                .publishOn(Schedulers.newSingle("publishOn"))
//                .subscribe(i -> System.out.println("subscribe:" + Thread.currentThread().getName()));


        //在这段代码中，使用publishOn指定了流发布的调度器，subscribeOn则指定的是流产生的调度器。
        //首先是parallel调度器进行流数据的生成，接着使用一个single单线程调度器进行发布，此时经过第一个map转换为另一个Flux流，其中的消息叠加了当前线程的名称。最后进入的是一个elastic弹性调度器，再次进行一次同样的map转换。
//        Flux.create(sink -> {
//                    sink.next(Thread.currentThread().getName());
//                    sink.complete();
//                })
//                .publishOn(Schedulers.single())
//                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
//                .publishOn(Schedulers.boundedElastic())
//                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
//                .subscribeOn(Schedulers.parallel())
//                .toStream()
//                .forEach(System.out::println);


       //fromExecutorService	自定义线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .map(i -> {
                    System.out.println("map:" + Thread.currentThread().getName());
                    return i;
                })
                .subscribeOn(Schedulers.fromExecutorService(executorService))
                .publishOn(Schedulers.fromExecutorService(executorService))
                .subscribe(i -> System.out.println(i + "-subscribe:" + Thread.currentThread().getName()));






    }

}
