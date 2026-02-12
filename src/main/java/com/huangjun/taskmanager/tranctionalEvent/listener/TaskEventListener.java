package com.huangjun.taskmanager.tranctionalEvent.listener;

import com.huangjun.taskmanager.constants.RedisConstants;
import com.huangjun.taskmanager.entity.Task;
import com.huangjun.taskmanager.tranctionalEvent.TaskUpdateEvent;
import com.huangjun.taskmanager.utils.RedisUtils;
import com.huangjun.taskmanager.utils.ThreadLocalUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
public class TaskEventListener {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void taskEventListener(TaskUpdateEvent event) {
        List<Task> taskList = event.getTaskList();
        for (Task task : taskList){
            RedisUtils.setHash(RedisConstants.TASK+ ThreadLocalUtils.getUser().getId(),task.getId(),task);
        }
        System.out.println("更新redis成功");
    }
}
