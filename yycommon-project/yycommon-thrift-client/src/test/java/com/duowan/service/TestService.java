/**
 * Autogenerated by Thrift Compiler (0.10.0)
 * <p>
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *
 * @generated
 */
package com.duowan.service;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2017-11-27")
public class TestService {

    public interface Iface {

        public String test(int length) throws org.apache.thrift.TException;

    }

    public interface AsyncIface {

        public void test(int length, org.apache.thrift.async.AsyncMethodCallback<String> resultHandler) throws org.apache.thrift.TException;

    }

    public static class Client extends org.apache.thrift.TServiceClient implements Iface {
        public static class Factory implements org.apache.thrift.TServiceClientFactory<Client> {
            public Factory() {
            }

            public Client getClient(org.apache.thrift.protocol.TProtocol prot) {
                return new Client(prot);
            }

            public Client getClient(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
                return new Client(iprot, oprot);
            }
        }

        public Client(org.apache.thrift.protocol.TProtocol prot) {
            super(prot, prot);
        }

        public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
            super(iprot, oprot);
        }

        public String test(int length) throws org.apache.thrift.TException {
            send_test(length);
            return recv_test();
        }

        public void send_test(int length) throws org.apache.thrift.TException {
            test_args args = new test_args();
            args.setLength(length);
            sendBase("test", args);
        }

        public String recv_test() throws org.apache.thrift.TException {
            test_result result = new test_result();
            receiveBase(result, "test");
            if (result.isSetSuccess()) {
                return result.success;
            }
            throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "test failed: unknown result");
        }

    }

    public static class AsyncClient extends org.apache.thrift.async.TAsyncClient implements AsyncIface {
        public static class Factory implements org.apache.thrift.async.TAsyncClientFactory<AsyncClient> {
            private org.apache.thrift.async.TAsyncClientManager clientManager;
            private org.apache.thrift.protocol.TProtocolFactory protocolFactory;

            public Factory(org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.protocol.TProtocolFactory protocolFactory) {
                this.clientManager = clientManager;
                this.protocolFactory = protocolFactory;
            }

            public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport transport) {
                return new AsyncClient(protocolFactory, clientManager, transport);
            }
        }

        public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.transport.TNonblockingTransport transport) {
            super(protocolFactory, clientManager, transport);
        }

        public void test(int length, org.apache.thrift.async.AsyncMethodCallback<String> resultHandler) throws org.apache.thrift.TException {
            checkReady();
            test_call method_call = new test_call(length, resultHandler, this, ___protocolFactory, ___transport);
            this.___currentMethod = method_call;
            ___manager.call(method_call);
        }

        public static class test_call extends org.apache.thrift.async.TAsyncMethodCall<String> {
            private int length;

            public test_call(int length, org.apache.thrift.async.AsyncMethodCallback<String> resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
                super(client, protocolFactory, transport, resultHandler, false);
                this.length = length;
            }

            public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
                prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("test", org.apache.thrift.protocol.TMessageType.CALL, 0));
                test_args args = new test_args();
                args.setLength(length);
                args.write(prot);
                prot.writeMessageEnd();
            }

            public String getResult() throws org.apache.thrift.TException {
                if (getState() != State.RESPONSE_READ) {
                    throw new IllegalStateException("Method call not finished!");
                }
                org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
                org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
                return (new Client(prot)).recv_test();
            }
        }

    }

    public static class Processor<I extends Iface> extends org.apache.thrift.TBaseProcessor<I> implements org.apache.thrift.TProcessor {
        private static final org.slf4j.Logger _LOGGER = org.slf4j.LoggerFactory.getLogger(Processor.class.getName());

        public Processor(I iface) {
            super(iface, getProcessMap(new java.util.HashMap<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>>()));
        }

        protected Processor(I iface, java.util.Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {
            super(iface, getProcessMap(processMap));
        }

        private static <I extends Iface> java.util.Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> getProcessMap(java.util.Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {
            processMap.put("test", new test());
            return processMap;
        }

        public static class test<I extends Iface> extends org.apache.thrift.ProcessFunction<I, test_args> {
            public test() {
                super("test");
            }

            public test_args getEmptyArgsInstance() {
                return new test_args();
            }

            protected boolean isOneway() {
                return false;
            }

            public test_result getResult(I iface, test_args args) throws org.apache.thrift.TException {
                test_result result = new test_result();
                result.success = iface.test(args.length);
                return result;
            }
        }

    }

    public static class AsyncProcessor<I extends AsyncIface> extends org.apache.thrift.TBaseAsyncProcessor<I> {
        private static final org.slf4j.Logger _LOGGER = org.slf4j.LoggerFactory.getLogger(AsyncProcessor.class.getName());

        public AsyncProcessor(I iface) {
            super(iface, getProcessMap(new java.util.HashMap<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>>()));
        }

        protected AsyncProcessor(I iface, java.util.Map<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>> processMap) {
            super(iface, getProcessMap(processMap));
        }

        private static <I extends AsyncIface> java.util.Map<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>> getProcessMap(java.util.Map<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>> processMap) {
            processMap.put("test", new test());
            return processMap;
        }

        public static class test<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, test_args, String> {
            public test() {
                super("test");
            }

            public test_args getEmptyArgsInstance() {
                return new test_args();
            }

            public org.apache.thrift.async.AsyncMethodCallback<String> getResultHandler(final org.apache.thrift.server.AbstractNonblockingServer.AsyncFrameBuffer fb, final int seqid) {
                final org.apache.thrift.AsyncProcessFunction fcall = this;
                return new org.apache.thrift.async.AsyncMethodCallback<String>() {
                    public void onComplete(String o) {
                        test_result result = new test_result();
                        result.success = o;
                        try {
                            fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
                        } catch (org.apache.thrift.transport.TTransportException e) {
                            _LOGGER.error("TTransportException writing to internal frame buffer", e);
                            fb.close();
                        } catch (Exception e) {
                            _LOGGER.error("Exception writing to internal frame buffer", e);
                            onError(e);
                        }
                    }

                    public void onError(Exception e) {
                        byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
                        org.apache.thrift.TSerializable msg;
                        test_result result = new test_result();
                        if (e instanceof org.apache.thrift.transport.TTransportException) {
                            _LOGGER.error("TTransportException inside handler", e);
                            fb.close();
                            return;
                        } else if (e instanceof org.apache.thrift.TApplicationException) {
                            _LOGGER.error("TApplicationException inside handler", e);
                            msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
                            msg = (org.apache.thrift.TApplicationException) e;
                        } else {
                            _LOGGER.error("Exception inside handler", e);
                            msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
                            msg = new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
                        }
                        try {
                            fcall.sendResponse(fb, msg, msgType, seqid);
                        } catch (Exception ex) {
                            _LOGGER.error("Exception writing to internal frame buffer", ex);
                            fb.close();
                        }
                    }
                };
            }

            protected boolean isOneway() {
                return false;
            }

            public void start(I iface, test_args args, org.apache.thrift.async.AsyncMethodCallback<String> resultHandler) throws org.apache.thrift.TException {
                iface.test(args.length, resultHandler);
            }
        }

    }

    public static class test_args implements org.apache.thrift.TBase<test_args, test_args._Fields>, java.io.Serializable, Cloneable, Comparable<test_args> {
        private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("test_args");

        private static final org.apache.thrift.protocol.TField LENGTH_FIELD_DESC = new org.apache.thrift.protocol.TField("length", org.apache.thrift.protocol.TType.I32, (short) 1);

        private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new test_argsStandardSchemeFactory();
        private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new test_argsTupleSchemeFactory();

        public int length; // required

        /**
         * The set of fields this struct contains, along with convenience methods for finding and manipulating them.
         */
        public enum _Fields implements org.apache.thrift.TFieldIdEnum {
            LENGTH((short) 1, "length");

            private static final java.util.Map<String, _Fields> byName = new java.util.HashMap<String, _Fields>();

            static {
                for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
                    byName.put(field.getFieldName(), field);
                }
            }

            /**
             * Find the _Fields constant that matches fieldId, or null if its not found.
             */
            public static _Fields findByThriftId(int fieldId) {
                switch (fieldId) {
                    case 1: // LENGTH
                        return LENGTH;
                    default:
                        return null;
                }
            }

            /**
             * Find the _Fields constant that matches fieldId, throwing an exception
             * if it is not found.
             */
            public static _Fields findByThriftIdOrThrow(int fieldId) {
                _Fields fields = findByThriftId(fieldId);
                if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
                return fields;
            }

            /**
             * Find the _Fields constant that matches name, or null if its not found.
             */
            public static _Fields findByName(String name) {
                return byName.get(name);
            }

            private final short _thriftId;
            private final String _fieldName;

            _Fields(short thriftId, String fieldName) {
                _thriftId = thriftId;
                _fieldName = fieldName;
            }

            public short getThriftFieldId() {
                return _thriftId;
            }

            public String getFieldName() {
                return _fieldName;
            }
        }

        // isset id assignments
        private static final int __LENGTH_ISSET_ID = 0;
        private byte __isset_bitfield = 0;
        public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;

        static {
            java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
            tmpMap.put(_Fields.LENGTH, new org.apache.thrift.meta_data.FieldMetaData("length", org.apache.thrift.TFieldRequirementType.DEFAULT,
                    new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
            metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
            org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(test_args.class, metaDataMap);
        }

        public test_args() {
        }

        public test_args(
                int length) {
            this();
            this.length = length;
            setLengthIsSet(true);
        }

        /**
         * Performs a deep copy on <i>other</i>.
         */
        public test_args(test_args other) {
            __isset_bitfield = other.__isset_bitfield;
            this.length = other.length;
        }

        public test_args deepCopy() {
            return new test_args(this);
        }

        @Override
        public void clear() {
            setLengthIsSet(false);
            this.length = 0;
        }

        public int getLength() {
            return this.length;
        }

        public test_args setLength(int length) {
            this.length = length;
            setLengthIsSet(true);
            return this;
        }

        public void unsetLength() {
            __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __LENGTH_ISSET_ID);
        }

        /**
         * Returns true if field length is set (has been assigned a value) and false otherwise
         */
        public boolean isSetLength() {
            return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __LENGTH_ISSET_ID);
        }

        public void setLengthIsSet(boolean value) {
            __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __LENGTH_ISSET_ID, value);
        }

        public void setFieldValue(_Fields field, Object value) {
            switch (field) {
                case LENGTH:
                    if (value == null) {
                        unsetLength();
                    } else {
                        setLength((Integer) value);
                    }
                    break;

            }
        }

        public Object getFieldValue(_Fields field) {
            switch (field) {
                case LENGTH:
                    return getLength();

            }
            throw new IllegalStateException();
        }

        /**
         * Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise
         */
        public boolean isSet(_Fields field) {
            if (field == null) {
                throw new IllegalArgumentException();
            }

            switch (field) {
                case LENGTH:
                    return isSetLength();
            }
            throw new IllegalStateException();
        }

        @Override
        public boolean equals(Object that) {
            if (that == null)
                return false;
            if (that instanceof test_args)
                return this.equals((test_args) that);
            return false;
        }

        public boolean equals(test_args that) {
            if (that == null)
                return false;
            if (this == that)
                return true;

            boolean this_present_length = true;
            boolean that_present_length = true;
            if (this_present_length || that_present_length) {
                if (!(this_present_length && that_present_length))
                    return false;
                if (this.length != that.length)
                    return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int hashCode = 1;

            hashCode = hashCode * 8191 + length;

            return hashCode;
        }

        @Override
        public int compareTo(test_args other) {
            if (!getClass().equals(other.getClass())) {
                return getClass().getName().compareTo(other.getClass().getName());
            }

            int lastComparison = 0;

            lastComparison = Boolean.valueOf(isSetLength()).compareTo(other.isSetLength());
            if (lastComparison != 0) {
                return lastComparison;
            }
            if (isSetLength()) {
                lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.length, other.length);
                if (lastComparison != 0) {
                    return lastComparison;
                }
            }
            return 0;
        }

        public _Fields fieldForId(int fieldId) {
            return _Fields.findByThriftId(fieldId);
        }

        public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
            scheme(iprot).read(iprot, this);
        }

        public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
            scheme(oprot).write(oprot, this);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("test_args(");
            boolean first = true;

            sb.append("length:");
            sb.append(this.length);
            first = false;
            sb.append(")");
            return sb.toString();
        }

        public void validate() throws org.apache.thrift.TException {
            // check for required fields
            // check for sub-struct validity
        }

        private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
            try {
                write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
            } catch (org.apache.thrift.TException te) {
                throw new java.io.IOException(te);
            }
        }

        private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
            try {
                // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
                __isset_bitfield = 0;
                read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
            } catch (org.apache.thrift.TException te) {
                throw new java.io.IOException(te);
            }
        }

        private static class test_argsStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
            public test_argsStandardScheme getScheme() {
                return new test_argsStandardScheme();
            }
        }

        private static class test_argsStandardScheme extends org.apache.thrift.scheme.StandardScheme<test_args> {

            public void read(org.apache.thrift.protocol.TProtocol iprot, test_args struct) throws org.apache.thrift.TException {
                org.apache.thrift.protocol.TField schemeField;
                iprot.readStructBegin();
                while (true) {
                    schemeField = iprot.readFieldBegin();
                    if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
                        break;
                    }
                    switch (schemeField.id) {
                        case 1: // LENGTH
                            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
                                struct.length = iprot.readI32();
                                struct.setLengthIsSet(true);
                            } else {
                                org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                            }
                            break;
                        default:
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                    }
                    iprot.readFieldEnd();
                }
                iprot.readStructEnd();

                // check for required fields of primitive type, which can't be checked in the validate method
                struct.validate();
            }

            public void write(org.apache.thrift.protocol.TProtocol oprot, test_args struct) throws org.apache.thrift.TException {
                struct.validate();

                oprot.writeStructBegin(STRUCT_DESC);
                oprot.writeFieldBegin(LENGTH_FIELD_DESC);
                oprot.writeI32(struct.length);
                oprot.writeFieldEnd();
                oprot.writeFieldStop();
                oprot.writeStructEnd();
            }

        }

        private static class test_argsTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
            public test_argsTupleScheme getScheme() {
                return new test_argsTupleScheme();
            }
        }

        private static class test_argsTupleScheme extends org.apache.thrift.scheme.TupleScheme<test_args> {

            @Override
            public void write(org.apache.thrift.protocol.TProtocol prot, test_args struct) throws org.apache.thrift.TException {
                org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
                java.util.BitSet optionals = new java.util.BitSet();
                if (struct.isSetLength()) {
                    optionals.set(0);
                }
                oprot.writeBitSet(optionals, 1);
                if (struct.isSetLength()) {
                    oprot.writeI32(struct.length);
                }
            }

            @Override
            public void read(org.apache.thrift.protocol.TProtocol prot, test_args struct) throws org.apache.thrift.TException {
                org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
                java.util.BitSet incoming = iprot.readBitSet(1);
                if (incoming.get(0)) {
                    struct.length = iprot.readI32();
                    struct.setLengthIsSet(true);
                }
            }
        }

        private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
            return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
        }
    }

    public static class test_result implements org.apache.thrift.TBase<test_result, test_result._Fields>, java.io.Serializable, Cloneable, Comparable<test_result> {
        private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("test_result");

        private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.STRING, (short) 0);

        private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new test_resultStandardSchemeFactory();
        private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new test_resultTupleSchemeFactory();

        public String success; // required

        /**
         * The set of fields this struct contains, along with convenience methods for finding and manipulating them.
         */
        public enum _Fields implements org.apache.thrift.TFieldIdEnum {
            SUCCESS((short) 0, "success");

            private static final java.util.Map<String, _Fields> byName = new java.util.HashMap<String, _Fields>();

            static {
                for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
                    byName.put(field.getFieldName(), field);
                }
            }

            /**
             * Find the _Fields constant that matches fieldId, or null if its not found.
             */
            public static _Fields findByThriftId(int fieldId) {
                switch (fieldId) {
                    case 0: // SUCCESS
                        return SUCCESS;
                    default:
                        return null;
                }
            }

            /**
             * Find the _Fields constant that matches fieldId, throwing an exception
             * if it is not found.
             */
            public static _Fields findByThriftIdOrThrow(int fieldId) {
                _Fields fields = findByThriftId(fieldId);
                if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
                return fields;
            }

            /**
             * Find the _Fields constant that matches name, or null if its not found.
             */
            public static _Fields findByName(String name) {
                return byName.get(name);
            }

            private final short _thriftId;
            private final String _fieldName;

            _Fields(short thriftId, String fieldName) {
                _thriftId = thriftId;
                _fieldName = fieldName;
            }

            public short getThriftFieldId() {
                return _thriftId;
            }

            public String getFieldName() {
                return _fieldName;
            }
        }

        // isset id assignments
        public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;

        static {
            java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
            tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT,
                    new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
            metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
            org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(test_result.class, metaDataMap);
        }

        public test_result() {
        }

        public test_result(
                String success) {
            this();
            this.success = success;
        }

        /**
         * Performs a deep copy on <i>other</i>.
         */
        public test_result(test_result other) {
            if (other.isSetSuccess()) {
                this.success = other.success;
            }
        }

        public test_result deepCopy() {
            return new test_result(this);
        }

        @Override
        public void clear() {
            this.success = null;
        }

        public String getSuccess() {
            return this.success;
        }

        public test_result setSuccess(String success) {
            this.success = success;
            return this;
        }

        public void unsetSuccess() {
            this.success = null;
        }

        /**
         * Returns true if field success is set (has been assigned a value) and false otherwise
         */
        public boolean isSetSuccess() {
            return this.success != null;
        }

        public void setSuccessIsSet(boolean value) {
            if (!value) {
                this.success = null;
            }
        }

        public void setFieldValue(_Fields field, Object value) {
            switch (field) {
                case SUCCESS:
                    if (value == null) {
                        unsetSuccess();
                    } else {
                        setSuccess((String) value);
                    }
                    break;

            }
        }

        public Object getFieldValue(_Fields field) {
            switch (field) {
                case SUCCESS:
                    return getSuccess();

            }
            throw new IllegalStateException();
        }

        /**
         * Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise
         */
        public boolean isSet(_Fields field) {
            if (field == null) {
                throw new IllegalArgumentException();
            }

            switch (field) {
                case SUCCESS:
                    return isSetSuccess();
            }
            throw new IllegalStateException();
        }

        @Override
        public boolean equals(Object that) {
            if (that == null)
                return false;
            if (that instanceof test_result)
                return this.equals((test_result) that);
            return false;
        }

        public boolean equals(test_result that) {
            if (that == null)
                return false;
            if (this == that)
                return true;

            boolean this_present_success = true && this.isSetSuccess();
            boolean that_present_success = true && that.isSetSuccess();
            if (this_present_success || that_present_success) {
                if (!(this_present_success && that_present_success))
                    return false;
                if (!this.success.equals(that.success))
                    return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int hashCode = 1;

            hashCode = hashCode * 8191 + ((isSetSuccess()) ? 131071 : 524287);
            if (isSetSuccess())
                hashCode = hashCode * 8191 + success.hashCode();

            return hashCode;
        }

        @Override
        public int compareTo(test_result other) {
            if (!getClass().equals(other.getClass())) {
                return getClass().getName().compareTo(other.getClass().getName());
            }

            int lastComparison = 0;

            lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
            if (lastComparison != 0) {
                return lastComparison;
            }
            if (isSetSuccess()) {
                lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
                if (lastComparison != 0) {
                    return lastComparison;
                }
            }
            return 0;
        }

        public _Fields fieldForId(int fieldId) {
            return _Fields.findByThriftId(fieldId);
        }

        public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
            scheme(iprot).read(iprot, this);
        }

        public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
            scheme(oprot).write(oprot, this);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("test_result(");
            boolean first = true;

            sb.append("success:");
            if (this.success == null) {
                sb.append("null");
            } else {
                sb.append(this.success);
            }
            first = false;
            sb.append(")");
            return sb.toString();
        }

        public void validate() throws org.apache.thrift.TException {
            // check for required fields
            // check for sub-struct validity
        }

        private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
            try {
                write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
            } catch (org.apache.thrift.TException te) {
                throw new java.io.IOException(te);
            }
        }

        private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
            try {
                read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
            } catch (org.apache.thrift.TException te) {
                throw new java.io.IOException(te);
            }
        }

        private static class test_resultStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
            public test_resultStandardScheme getScheme() {
                return new test_resultStandardScheme();
            }
        }

        private static class test_resultStandardScheme extends org.apache.thrift.scheme.StandardScheme<test_result> {

            public void read(org.apache.thrift.protocol.TProtocol iprot, test_result struct) throws org.apache.thrift.TException {
                org.apache.thrift.protocol.TField schemeField;
                iprot.readStructBegin();
                while (true) {
                    schemeField = iprot.readFieldBegin();
                    if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
                        break;
                    }
                    switch (schemeField.id) {
                        case 0: // SUCCESS
                            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
                                struct.success = iprot.readString();
                                struct.setSuccessIsSet(true);
                            } else {
                                org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                            }
                            break;
                        default:
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                    }
                    iprot.readFieldEnd();
                }
                iprot.readStructEnd();

                // check for required fields of primitive type, which can't be checked in the validate method
                struct.validate();
            }

            public void write(org.apache.thrift.protocol.TProtocol oprot, test_result struct) throws org.apache.thrift.TException {
                struct.validate();

                oprot.writeStructBegin(STRUCT_DESC);
                if (struct.success != null) {
                    oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
                    oprot.writeString(struct.success);
                    oprot.writeFieldEnd();
                }
                oprot.writeFieldStop();
                oprot.writeStructEnd();
            }

        }

        private static class test_resultTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
            public test_resultTupleScheme getScheme() {
                return new test_resultTupleScheme();
            }
        }

        private static class test_resultTupleScheme extends org.apache.thrift.scheme.TupleScheme<test_result> {

            @Override
            public void write(org.apache.thrift.protocol.TProtocol prot, test_result struct) throws org.apache.thrift.TException {
                org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
                java.util.BitSet optionals = new java.util.BitSet();
                if (struct.isSetSuccess()) {
                    optionals.set(0);
                }
                oprot.writeBitSet(optionals, 1);
                if (struct.isSetSuccess()) {
                    oprot.writeString(struct.success);
                }
            }

            @Override
            public void read(org.apache.thrift.protocol.TProtocol prot, test_result struct) throws org.apache.thrift.TException {
                org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
                java.util.BitSet incoming = iprot.readBitSet(1);
                if (incoming.get(0)) {
                    struct.success = iprot.readString();
                    struct.setSuccessIsSet(true);
                }
            }
        }

        private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
            return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
        }
    }

}
