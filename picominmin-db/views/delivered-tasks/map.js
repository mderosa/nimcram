function(doc) {
  if (doc.type == 'task' && doc.taskCompleteDate && !doc.taskTerminateDate) {
    var temp = [doc.project].concat(doc['taskCompleteDate']);
    emit(temp, doc);
  }
}