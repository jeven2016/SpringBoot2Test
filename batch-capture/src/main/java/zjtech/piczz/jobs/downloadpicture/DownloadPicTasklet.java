package zjtech.piczz.jobs.downloadpicture;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DownloadPicTasklet implements Tasklet {

  @Autowired
  DownloadingThreadPool downloadingThreadPool;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    downloadingThreadPool.run();
    return RepeatStatus.FINISHED;
  }
}
