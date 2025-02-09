package parttimejob.Utils.thread;

import org.springframework.stereotype.Component;

@Component
public  class BaseThread {

    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setThreadLocal(Long id){
        threadLocal.set(id);
    }
    public static Long getThreadLocal(){
      return threadLocal.get();
    }
}
