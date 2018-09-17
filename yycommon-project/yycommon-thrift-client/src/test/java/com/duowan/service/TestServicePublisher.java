package com.duowan.service;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TServerSocket;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 22:09
 */
public class TestServicePublisher {

    public static void main(String[] args) throws Exception {
        //simplePublishByRoot(25000);

        simplePublishByRouter(25000, "test");
    }

    public static void simplePublishByRoot(int port) throws Exception {
        TServerSocket serverTransport = new TServerSocket(port);
        TBinaryProtocol.Factory portFactory = new TBinaryProtocol.Factory(true, true);
        TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
        TestService.Processor<TestService.Iface> processor = new TestService.Processor<TestService.Iface>(new TestServiceImpl());

        args.processor(processor);
        args.protocolFactory(portFactory);
        TServer server = new TThreadPoolServer(args);

        server.serve();
    }

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
}
