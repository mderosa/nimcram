function(doc) {
  if (doc.type == 'task' && 
      doc.progress == 'proposed' &&
      !doc.taskStartDate && !doc.taskTerminateDate) {
    if (doc.priority == null) {
      emit([doc.project, 0], doc);
    } else {
      emit([doc.project, doc.priority], doc);
    }
  }
}