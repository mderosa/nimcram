function(doc) {
  if (doc.type == 'user') {
    emit(doc.email, doc);
  }
}