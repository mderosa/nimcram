function(doc) {
  if (doc.type === "version") {
  	emit(doc.type, doc.version);
  }
}