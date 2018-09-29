package com.duowan.service;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.*;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * HTTP
 * https://wiki.apache.org/thrift/Thrift%20&%20Eclipse%20&%20JUnit%20with%20TServlet
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 22:09
 */
public class TestServicePublisher {

    public static void main(String[] args) throws Exception {

        int serverPort = 25000;
        String router = "test";

        // 服务模型 - 单线程阻塞式
        singlePublishBySingle(serverPort);
        //simplePublishByRouter(serverPort, router);
        //simplePublishByRouters(serverPort, "test1", "test2");

        // 服务模型 - 线程池
        //threadPoolPublishBySingle(serverPort);
        //threadPoolPublishByRouter(serverPort, router);

        // 服务模型 - 单线程非阻塞式
        //nonblockingPublishBySingle(serverPort);
        //nonblockingPublishByRouter(serverPort, router);

        // 服务模型 - 线程池选择器
        //threadedSelectorPublishByRouter(serverPort);
        //threadedSelectorPublishByRouter(serverPort, "test");

        // 服务模型 - 半同步半异步
        //hsHaPublishBySingle(serverPort);
        //hsHaPublishByRouter(serverPort, router);

    }

    public static void simplePublishByRouters(int port, String router1, String router2) throws Exception {
        TServerSocket serverTransport = new TServerSocket(port);
        TSimpleServer.Args args = new TSimpleServer.Args(serverTransport);
        args.transportFactory(new TFastFramedTransport.Factory());
        args.protocolFactory(new TCompactProtocol.Factory());

        TestService.Processor<TestService.Iface> processor1 = new TestService.Processor<TestService.Iface>(new TestServiceImpl());
        TestService2.Processor<TestService2.Iface> processor2 = new TestService2.Processor<TestService2.Iface>(new TestService2Impl());

        TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
        multiplexedProcessor.registerProcessor(router1, processor1);
        multiplexedProcessor.registerProcessor(router2, processor2);

        args.processor(multiplexedProcessor);
        TServer server = new TSimpleServer(args);

        server.serve();
    }

    public static void singlePublishBySingle(int port) throws Exception {
        TServerSocket serverTransport = new TServerSocket(port);
        TBinaryProtocol.Factory portFactory = new TBinaryProtocol.Factory(true, true);
        TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
        TestService.Processor<TestService.Iface> processor = new TestService.Processor<TestService.Iface>(new TestServiceImpl());

        args.processor(processor);
        args.protocolFactory(portFactory);
        TServer server = new TThreadPoolServer(args);

        server.serve();
    }

    /**
     * publish by TSimpleServer and TMultiplexedProcessor
     *
     * @param port   port
     * @param router router
     * @throws Exception any Exception
     */
    public static void simplePublishByRouter(int port, String router) throws Exception {
        TServerSocket serverTransport = new TServerSocket(port);
        TSimpleServer.Args args = new TSimpleServer.Args(serverTransport);
        args.transportFactory(new TFastFramedTransport.Factory());
        args.protocolFactory(new TCompactProtocol.Factory());

        TestService.Processor<TestService.Iface> singleProcessor = new TestService.Processor<TestService.Iface>(new TestServiceImpl());
        TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
        multiplexedProcessor.registerProcessor(router, singleProcessor);

        args.processor(multiplexedProcessor);
        TServer server = new TSimpleServer(args);

        server.serve();
    }

    /**
     * publish by TThreadPoolServer and TMultiplexedProcessor
     *
     * @param port   port
     * @param router router
     * @throws Exception any Exception
     */
    public static void threadPoolPublishByRouter(int port, String router) throws Exception {
        TServerSocket serverTransport = new TServerSocket(port);
        TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
        args.transportFactory(new TFastFramedTransport.Factory());
        args.protocolFactory(new TCompactProtocol.Factory());

        TestService.Processor<TestService.Iface> singleProcessor = new TestService.Processor<TestService.Iface>(new TestServiceImpl());
        TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
        multiplexedProcessor.registerProcessor(router, singleProcessor);

        args.processor(multiplexedProcessor);
        TServer server = new TThreadPoolServer(args);

        server.serve();
    }

    /**
     * publish by TThreadPoolServer and TProcessor
     *
     * @param port port
     * @throws Exception any Exception
     */
    public static void threadPoolPublishBySingle(int port) throws Exception {
        TServerSocket serverTransport = new TServerSocket(port);
        TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
        args.transportFactory(new TFastFramedTransport.Factory());
        args.protocolFactory(new TCompactProtocol.Factory());

        TestService.Processor<TestService.Iface> singleProcessor = new TestService.Processor<TestService.Iface>(new TestServiceImpl());

        args.processor(singleProcessor);
        TServer server = new TThreadPoolServer(args);

        server.serve();
    }

    /**
     * publish by TNonblockingServer and TMultiplexedProcessor
     *
     * @param port   port
     * @param router router
     * @throws Exception any Exception
     */
    public static void nonblockingPublishByRouter(int port, String router) throws Exception {
        TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
        TNonblockingServer.Args args = new TNonblockingServer.Args(serverTransport);

        args.transportFactory(new TFastFramedTransport.Factory());
        args.protocolFactory(new TCompactProtocol.Factory());

        TestService.Processor<TestService.Iface> singleProcessor = new TestService.Processor<TestService.Iface>(new TestServiceImpl());
        TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
        multiplexedProcessor.registerProcessor(router, singleProcessor);

        args.processor(multiplexedProcessor);
        TServer server = new TNonblockingServer(args);

        server.serve();
    }

    /**
     * publish by TNonblockingServer and TProcessor
     *
     * @param port port
     * @throws Exception any Exception
     */
    public static void nonblockingPublishBySingle(int port) throws Exception {
        TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
        TNonblockingServer.Args args = new TNonblockingServer.Args(serverTransport);

        args.transportFactory(new TFastFramedTransport.Factory());
        args.protocolFactory(new TCompactProtocol.Factory());

        TestService.Processor<TestService.Iface> singleProcessor = new TestService.Processor<TestService.Iface>(new TestServiceImpl());

        args.processor(singleProcessor);
        TServer server = new TNonblockingServer(args);

        server.serve();
    }

    /**
     * publish by TThreadedSelectorServer and TProcessor
     *
     * @param port port
     * @throws Exception any Exception
     */
    public static void threadedSelectorPublishByRouter(int port) throws Exception {
        TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
        TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(serverTransport);

        args.transportFactory(new TFastFramedTransport.Factory());
        args.protocolFactory(new TCompactProtocol.Factory());
        args.selectorThreads(2);
        args.workerThreads(5);
        args.acceptQueueSizePerThread(1000);

        args.executorService(new ThreadPoolExecutor(5, 20, 5, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(1000)));

        TestService.Processor<TestService.Iface> singleProcessor = new TestService.Processor<TestService.Iface>(new TestServiceImpl());

        args.processor(singleProcessor);
        TServer server = new TThreadedSelectorServer(args);

        server.serve();
    }

    /**
     * publish by TThreadedSelectorServer and TMultiplexedProcessor
     *
     * @param port   port
     * @param router router
     * @throws Exception any Exception
     */
    public static void threadedSelectorPublishByRouter(int port, String router) throws Exception {
        TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
        TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(serverTransport);

        args.transportFactory(new TFastFramedTransport.Factory());
        args.protocolFactory(new TCompactProtocol.Factory());
        args.selectorThreads(2);
        args.workerThreads(5);
        args.acceptQueueSizePerThread(1000);

        args.executorService(new ThreadPoolExecutor(5, 20, 5, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(1000)));

        TestService.Processor<TestService.Iface> singleProcessor = new TestService.Processor<TestService.Iface>(new TestServiceImpl());
        TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
        multiplexedProcessor.registerProcessor(router, singleProcessor);

        args.processor(multiplexedProcessor);
        TServer server = new TThreadedSelectorServer(args);

        server.serve();
    }

    /**
     * publish by THsHaServer and TProcessor
     *
     * @param port port
     * @throws Exception any Exception
     */
    public static void hsHaPublishBySingle(int port) throws Exception {
        TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
        THsHaServer.Args args = new THsHaServer.Args(serverTransport);

        args.transportFactory(new TFastFramedTransport.Factory());
        args.protocolFactory(new TCompactProtocol.Factory());

        args.executorService(new ThreadPoolExecutor(5, 20, 5, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(1000)));

        TestService.Processor<TestService.Iface> singleProcessor = new TestService.Processor<TestService.Iface>(new TestServiceImpl());

        args.processor(singleProcessor);
        TServer server = new THsHaServer(args);

        server.serve();
    }

    /**
     * publish by THsHaServer and TMultiplexedProcessor
     *
     * @param port   port
     * @param router router
     * @throws Exception any Exception
     */
    public static void hsHaPublishByRouter(int port, String router) throws Exception {
        TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
        THsHaServer.Args args = new THsHaServer.Args(serverTransport);

        args.transportFactory(new TFastFramedTransport.Factory());
        args.protocolFactory(new TCompactProtocol.Factory());

        args.executorService(new ThreadPoolExecutor(5, 20, 5, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(1000)));

        TestService.Processor<TestService.Iface> singleProcessor = new TestService.Processor<TestService.Iface>(new TestServiceImpl());
        TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
        multiplexedProcessor.registerProcessor(router, singleProcessor);

        args.processor(multiplexedProcessor);
        TServer server = new THsHaServer(args);

        server.serve();
    }
}
