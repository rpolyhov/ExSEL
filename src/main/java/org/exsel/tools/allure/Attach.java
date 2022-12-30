package org.exsel.tools.allure;

import lombok.Getter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.exsel.tools.allure.Attachments.attachMessage;
import static org.exsel.tools.allure.Attachments.attachText;


/**
 * Created by SBT-Konovalov-GV on 23.05.2017.
 */
public class Attach{

    //public final static AttachIfError ifError = new AttachIfError();
    //public final static AttachIfErrorManual ifErrorManual = new AttachIfErrorManual();
    //public final static AttachLastIfError lastIfError = new AttachLastIfError();
    //public final static AttachStep step = new AttachStep();

    private static ThreadLocal<AttachIfError> localThreadIfError = new ThreadLocal<>();
    private static ThreadLocal<AttachIfErrorManual> localThreadIfErrorManual = new ThreadLocal<>();
    private static ThreadLocal<AttachLastIfError> localThreadLastIfError = new ThreadLocal<>();
    private static ThreadLocal<AttachStep> localThreadStep = new ThreadLocal<>();

    private static ThreadLocal<AssumeStep> localThreadAssumeStep = new ThreadLocal<>();

    private static ThreadLocal<List<StepError>> localThreadAssumeExceptions = new ThreadLocal<>();

    @Getter
    private static class StepError{
        private Throwable er;
        private String stepName;

        StepError(String stepError, Throwable er){
            this.er = er;
            this.stepName = stepError;
        }
    }

    public synchronized static AttachIfError ifError(){
        if(localThreadIfError.get() == null)
            localThreadIfError.set(new AttachIfError());
        return localThreadIfError.get();
    }

    public synchronized static AttachIfErrorManual ifErrorManual(){
        if(localThreadIfErrorManual.get() == null)
            localThreadIfErrorManual.set(new AttachIfErrorManual());
        return localThreadIfErrorManual.get();
    }

    public synchronized static AttachLastIfError lastIfError(){
        if(localThreadLastIfError.get() == null)
            localThreadLastIfError.set(new AttachLastIfError());
        return localThreadLastIfError.get();
    }

    public synchronized static AttachStep step(){
        if(localThreadStep.get() == null)
            localThreadStep.set(new AttachStep());
        return localThreadStep.get();
    }

    public synchronized static AssumeStep assume(){
        if(localThreadAssumeStep.get() == null)
            localThreadAssumeStep.set(new AssumeStep());
        return localThreadAssumeStep.get();
    }


    public static void clearCommandsAll(){
        clearCommands();
        clearManualCommands();
        clearLastCommands();
    }

    public static void clearCommands(){
        //ifError.commandList = null;
        if(ifError().commandList != null)
            ifError().commandList.clear();
        if(ifError().commandMap != null)
            ifError().commandMap.clear();
    }

    public static void clearManualCommands(){
        if(ifErrorManual().commandList != null)
            ifErrorManual().commandList.clear();
    }

    private static void clearLastCommands(){
        if(ifErrorManual().commandList != null)
            ifErrorManual().commandList.clear();
    }


    public static void executeIfError(){
        // Выполняем и чистим команду
        if(lastIfError().command != null) {
            lastIfError().command.get();
            lastIfError().command = null;
        }

        // Выполняем и чистим список команд
        if(ifError().commandList != null) {
            for(int i = 0; i < ifError().commandList.size(); i ++)
                ifError().commandList.get(i).get();
            ifError().commandList = null;
        }

        // Выполняем и чистим map команд
        if(ifError().commandMap != null) {
            for(Supplier supplier: ifError().commandMap.values())
                try {
                    supplier.get();
                }catch (Exception e){e.printStackTrace();}
            ifError().commandMap = null;
        }

        // Выполняем список команд
        if(ifErrorManual().commandList != null) {
            for(int i = 0; i < ifErrorManual().commandList.size(); i ++)
                ifErrorManual().commandList.get(i).get();
        }
    }

    public static Error doAssume(){
        if(localThreadAssumeExceptions.get() == null)
            return null;
        if(localThreadAssumeExceptions.get().size() == 0)
            return null;

        List<StepError> errorList = localThreadAssumeExceptions.get();

        StringBuilder sb = new StringBuilder("The following asserts failed:\n\t");

        Iterator<StepError> it =  errorList.iterator();

        List<Throwable> throwables = new ArrayList<>();

        while(it.hasNext()) {
            StepError stepError = it.next();
            sb.append("\n\t")
                .append(stepError.er.getMessage())
                .append("\n\t");

            throwables.add(stepError.er);
        }

        localThreadAssumeExceptions.get().clear();

        AssertionError error = new AssertionError(sb.toString());

        for(Throwable th: throwables)
            error.addSuppressed(th);

        return error;
    }

    /**
     * Абстрактный класс для работы с разными аттачами
     */
    public static abstract class AttachAbstractCommand {

        protected abstract <T> void attach(Supplier<T> command);

        protected abstract <T> void attach(String key, Supplier<T> command);

        public void message(String message){
            attach(()-> attachMessage(message));
        }

        public void message(String title, String message){
            attach(()-> attachMessage(title, message));
        }

        public void saveAsXml(String title, String message){
            //attach(()-> Attachments.attachMessage(title, message, "image/svg+xml"));
            attach(title, ()-> attachMessage(title, message, "image/svg+xml"));
        }

        public void saveAsTxt(String title, String message){
            execute(e -> attachText(title, message));
        }

        public void saveAsCsv(String title, String message){
            attach(title, ()-> attachMessage(title, message, "text/csv"));
        }

        public void saveAsPng(String fileName){
            attach(pngFromFile("png attachment", fileName));
        }

        public void saveAsPng(String title, String fileName){
            attach(title, pngFromFile(title, fileName));
        }

        private static Supplier pngFromFile(String title, String fileName){
            return (() -> {
                try {
                    Attachments.savePngAttachment(title, fileName);
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
                return null;
            });
        }

        public void saveAsPng(byte[] bytes){
            attach(()-> Attachments.savePngAttachment(bytes));
        }

        public void saveAsPng(String title, byte[] bytes){
            attach(title, ()-> Attachments.savePngAttachment(title, bytes));
        }

//        public <T> void execute(Supplier<T> supplier){
//            attach(supplier);
//        }

        public void execute(Consumer consumer){
            attach(()-> {consumer.accept(this); return null;});
        }
        public void execute(String attachName, Consumer consumer){
            attach(attachName, ()-> {consumer.accept(this); return null;});
        }
    }

    /**
     * Добавляем последнюю команду после ошибки
     */
    public static class AttachLastIfError extends AttachAbstractCommand{

        protected Supplier command;

        protected <T> void attach(Supplier<T> command){
            this.command = command;
        }

        @Override
        protected <T> void attach(String key, Supplier<T> command) {
            this.attach(command);
        }
    }


    /**
     * Добавляем список команд после ошибки
     */
    public static class AttachIfError extends AttachAbstractCommand{

        protected List<Supplier> commandList;

        protected Map<String, Supplier> commandMap;

        protected <T> void attach(Supplier<T> command){
            if(commandList == null)
                commandList = new ArrayList<>();
            commandList.add(command);
        }

        @Override
        protected <T> void attach(String key, Supplier<T> command) {
            if(commandMap == null)
                commandMap = new HashMap<>();
            commandMap.put(key, command);
        }
    }

    /**
     * Добавляем список команд после ошибки (списки из этого класса чистяится ручками)
     */
    public static class AttachIfErrorManual extends AttachAbstractCommand{

        private List<Supplier> commandList;

        protected <T> void attach(Supplier<T> command){
            if(commandList == null)
                commandList = new ArrayList<>();
            commandList.add(command);
        }

        @Override
        protected <T> void attach(String key, Supplier<T> command) {
            this.attach(command);
        }
    }

    public static class AttachStep{

        //TODO подумать как переделать
       // @Step("{message}")
        public void message(String message) {
        }

       // @Step("{message}")
        public void message(Consumer consumer, String message) {
            consumer.accept(null);
        }

        public AttachStepNode parent(String message) {
            AttachStepNode stepNode = new AttachStepNode();
            stepNode.parent = message;
            return stepNode;
        }

        public class AttachStepNode{
            public String parent;
            private List<String> list = new ArrayList<>();

            public AttachStepNode addChild(String message){
                list.add(message);
                return this;
            }

            public void print(){
                printChild(parent);
            }

            //TODO
            //@Step("{0}")
            private void printChild(String parent){
                for(String m: list)
                    new AttachStep().message(m);
            }
        }

//        @Step("{0}")
//        public void message(String message, Supplier supplier){
//            supplier.get();
//        };

//        @Step("{0}")
//        public void message(String message, List<Supplier> supplierList){
//            for(Supplier s: supplierList)
//                s.get();
//        };
    }

    private static void addException(StepError stepError){
        if(localThreadAssumeExceptions.get() == null)
            localThreadAssumeExceptions.set(new ArrayList<>());
        localThreadAssumeExceptions.get().add(stepError);
    }

    public static class AssumeStep extends AttachStep{

        //@Step("{message}")
        public void message(Consumer consumer, String message) {
            try {
                consumer.accept(null);
            }catch (Throwable e){
                addException( new StepError(message, e));
            }
        }

        public void method(Consumer consumer) {
            try {
                consumer.accept(null);
            }catch (Error e){
                addException( new StepError(consumer.toString(), e));
            }
        }

        public void throwable(Throwable th) throws Throwable{
            try {
                throw th;
            }catch (Error e){
                addException( new StepError("throw", e));
            }catch (Throwable e){
                //e.printStackTrace();
                throw e;
            }
        }
    }
}
