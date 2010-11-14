function(doc) {
  if (doc.type == 'task' && 
      doc.taskStartDate &&
      !doc.taskCompleteDate && !doc.taskTerminateDate) {
    emit([doc.project].concat(doc['taskStartDate']), doc);
  }
}