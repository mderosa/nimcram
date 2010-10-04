
YUI.add('taskpanel', function(Y) {
	Y.namespace('hokulea');
	/**
	 * a class that contains one or more task lists and coordinates movements
	 * between the lists
	 * @param config {Object} this object contains the fields
	 * {lists: {name: TaskList}} 
	 */
	Y.hokulea.TaskPanel = new function(config) {
		this.config = config;
	};
	
	Y.hokulea.TaskPanel.prototype = {
		addNewTask: function (taskListName, task) {
			var list = null;
			this.config.yui.each(this.config.lists, function(list, idx) {
				if (list.name == taskListName) {
					return list;
				}
			});
			if (list != null) {
				list.addNewTask(task);
			}
		}
	};

}, '0.1');
