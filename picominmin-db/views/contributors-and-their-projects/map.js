function(doc) {
    if (doc.type === 'project') {
	var contributors = doc.contributors;
	for (var i = 0; i < contributors.length; i++) {
            emit(contributors[i], doc._id);
	}
    }
}