package com.example.test.Controllers;

import com.example.test.Entities.ITaskRepo;
import com.example.test.Entities.TaskModel;
import com.example.test.utils.Utils;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.RequestContextFilter;
import com.example.test.utils.Utils;

import javax.management.remote.JMXServerErrorException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepo taskRepo;
    @Autowired
    private RequestContextFilter requestContextFilter;

    @PostMapping
public ResponseEntity createTask(@RequestBody TaskModel taskModel, HttpServletRequest request){

        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID)idUser);
        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt())|| currentDate.isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio/término deve ser superior à " +
                    "atual");
        }
        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor do que a data " +
                    "de termino");
        }
    var task=this.taskRepo.save(taskModel);
    return ResponseEntity.status(HttpStatus.OK).body(task);
}
@GetMapping
public List<TaskModel> list(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var tasks=this.taskRepo.findByIdUser((UUID) idUser);
        return tasks;
}
@PutMapping("/{id}")
public ResponseEntity update(@RequestBody TaskModel taskModel,HttpServletRequest request,@PathVariable UUID id){
    var task = this.taskRepo.findById(id).orElse(null);
    if (task==null){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
    }
    var idUser = request.getAttribute("idUser");
    if(!task.getIdUser().equals(idUser)){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não possui permissão para altera essa tarefa");
    }

    Utils.copyNonNullProperties(taskModel,task);
    taskModel.setIdUser((UUID)idUser);
    taskModel.setTaskId(id);
    var taskUpdated= this.taskRepo.save(taskModel);
return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
}
}
