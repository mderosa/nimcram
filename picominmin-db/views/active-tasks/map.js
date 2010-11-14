function(doc) {
  if (doc.type == 'task' && doc['taskCompleteDate'] == null) {
    emit(doc.project, doc);
  }
}