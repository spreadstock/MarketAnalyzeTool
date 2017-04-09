package com.cooeration.marketengine.core.hotdeploy;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryWatcher
{
    Logger logger = LoggerFactory.getLogger(DirectoryWatcher.class);
    private WatchService watcher;
    private Path path;
    private WatchKey key;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Deployer deployer;
    private String dir;


    public DirectoryWatcher(String dir) throws IOException
    {
        this.dir = dir;
        watcher = FileSystems.getDefault().newWatchService();
        path = Paths.get(this.dir);
        // 监控目录内文件的更新、创建和删除事件
        key = path.register(watcher, ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE);
    }


    /**
     * 启动监控过程
     */
    public void execute()
    {
        // 通过线程池启动一个额外的线程加载Watching过程
        executor.execute(() -> {
            processEvents();
        });
    }


    /**
     * 关闭后的对象无法重新启动
     * 
     * @throws IOException
     */
    public void shutdown() throws IOException
    {
        watcher.close();
        executor.shutdownNow();
    }

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event)
    {
        return (WatchEvent<T>) event;
    }

    /**
     * 监控文件系统事件
     */
    void processEvents()
    {
        while (true)
        {
            // 等待直到获得事件信号
            WatchKey signal;
            try
            {
                signal = watcher.take();
            }
            catch (InterruptedException x)
            {
                return;
            }

            for (WatchEvent<?> event : signal.pollEvents())
            {
                Kind<?> kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW)
                {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                notifiy(name.getFileName().toString(), kind);
                //only handle once for a group of OS event
                break;
            }
            // 为监控下一个通知做准备
            key.reset();
        }
    }


    /**
     * 通知外部各个Observer目录有新的事件更新
     */
    void notifiy(String fileName, Kind<?> kind)
    {
        logger.info(fileName + " has been "+kind+"\n");
        // 创建事件
        if (kind == StandardWatchEventKinds.ENTRY_CREATE)
        {
            deployer.deployAlgorithm(fileName);
        }
        // 修改事件
        if (kind == StandardWatchEventKinds.ENTRY_MODIFY)
        {
            deployer.redeployAlgorithm(fileName);
        }
        // 删除事件
        if (kind == StandardWatchEventKinds.ENTRY_DELETE)
        {
            deployer.undeployAlgorithm(fileName);
        }
    }


    public String getDir()
    {
        return dir;
    }


    public Deployer getDeployer()
    {
        return deployer;
    }


    public void setDeployer(Deployer deployer)
    {
        this.deployer = deployer;
    }

}