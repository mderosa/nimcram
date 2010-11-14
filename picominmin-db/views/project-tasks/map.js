function(doc) {
  if (doc.project && doc.type=='task') {
    emit(doc.project, doc);
  }
}