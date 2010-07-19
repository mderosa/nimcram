var y;

/**
 * This object represents a listing of tasks
 * <p>
 * config :: {root: Node, dragSelector: String, dropSelector: String}
 * root = the node that contains the contents of the table, right now this is a <td> 
 */
var TaskList = function(config) {
	this.config = config;
};
TaskList.prototype = {
	_makeNodesDraggable : function() {
		var ns = Y.Node.all(this.config.dragSelector);
    	Y.each(ns, function(v, k) {
	    	var drag = new Y.DD.Drag({
	    		node: v,
	    		target: {
	    			border: '0 0 0 20'
	    		}
	    	}).plug(Y.Plugin.DDProxy, {moveOnEnd: false});
	    });
	},
	
	_makeNodesDroppable: function() {
		var ns = Y.Node.all(this.config.dropSelector);
	    Y.each(ns, function(v, k) {
	        var drop = new Y.DD.Drop({node: v});       
	    });
	}
};

/**
 * This object represents a form which can be used to create a new task
 * <p>
 * config :: {root: Node, uri: String}
 * root = the node that will become the parent node of the form when it is created
 * uri = the uri to submit form data to
 */
var NewTaskForm = function(config) {
	this.config = config;
	this.visible = false;
}
NewTaskForm.prototype = {
	_addSubmitHandler: function(Y) {
			var nodBtn = Y.one('#newTaskSubmitter').on("click", function() {
			var cfg = {
				method: 'POST',
				on: {
					success: function(id, rsp, args) {
						console.log('success');
					},
					failure: function(id, rsp, args) {
						console.log('failure');
					},
					complete: function(id, rsp, args) {
						console.log('complete');
					}
				},
				form: {id: 'newTaskForm'}
			};
			//Y.io(this.config.uri, cfg);
		});
	},
	_addCancelHandler: function(Y) {
		var that = this;
		var nodAnchor = Y.one("#newTaskCanceler").on('click', function() {
			that.destroy(Y);
		});
	},
	_buildFormHtml: function(Y) {
		var nodNewTask = Y.Node.create(
			'<form id="newTaskForm" method="POST" action="#">' +
				'<input type="hidden" name="r_method" value="put" />' +
				'<label for="title">title:</label>' +
				'<input type="text" id="title" name="title"></input>' +
				'<label for="description">description:</label>' +
				'<textarea id="description" name="description"></textarea>' +
				'<fieldset><legend>provides end user functionality</legend>' +
					'<label>yes</label>' +
					'<input type="radio" name="deliversCustomerFunctionality" value="true" />' +
					'<label>no</label>' +
					'<input type="radio" name="deliversCustomerFunctionality" value="false" />' +
				'</fieldset>' +
				'<button id="newTaskSubmitter" type="button">create</button>' +
				'&nbsp;&nbsp;<a id="newTaskCanceler" href="#">cancel</a>' +
			'</form>');
		return nodNewTask;
	},
	show : function(Y) {
		if (this.visible == false) {
			var nodNewTask = this._buildFormHtml(Y);
			var nodTasks = this.config.root;
			if (nodTasks.hasChildNodes()) {
				nodTasks.prepend(nodNewTask);
			} else {
				nodTasks.appendChild(nodNewTask);
			}
			
			this._addSubmitHandler(Y);
			this._addCancelHandler(Y);
			this.visible = true;
		}
	},
	destroy: function(Y) {
		Y.one("#newTaskForm").remove();
		this.visible = false;
	}

};


YUI().use('dd-drop', 'dd-proxy', 'node-base', 'io', function(Y) {
	y = Y;
	var newTaskForm = new NewTaskForm({
			root: Y.one('#backburner div.tasks'),
			uri: "projects/" + serverData.project-name + "/tasks"
		});
	var waitingTasks = new TaskList({root: Y.one('#backburner')});
	var inProgressTasks = new TaskList({root: Y.one('#active')});
	var deliveredTasks = new TaskList({root: Y.one('#live')});
	
    var tasks = Y.Node.all('div.tasks table.task');
    Y.each(tasks, function(v, k) {
    	var drag = new Y.DD.Drag({
    		node: v,
    		target: {
    			border: '0 0 0 20'
    		}
    	}).plug(Y.Plugin.DDProxy, {moveOnEnd: false});
    });

    var tasksDiv = Y.Node.all('table.task, div.bmrcp-head');
    Y.each(tasksDiv, function(v, k) {
        var drop = new Y.DD.Drop({node: v});       
    });

   function resizeTasks() {
	   var heightY = Y.DOM.winHeight();
	   var ls = Y.all('div.tasks');
	   ls.setStyle('height', (heightY - 175));
   };   

   
   Y.one('#new').on('click', function() {
   		newTaskForm.show(Y);
   });
   
   Y.on('resize', function(e){
	   resizeTasks();
   });

   Y.DD.DDM.on('drag:start', function(e) {
	    var drag = e.target;
	    drag.get('node').setStyle('opacity', '.25');
	    drag.get('dragNode').set('innerHTML', drag.get('node').get('innerHTML'));
	    drag.get('dragNode').setStyles({opacity: '.65'});
	});
   
   Y.DD.DDM.on('drag:end', function(e) {
	    var drag = e.target;
	    drag.get('node').setStyles({opacity: '1'});
	});
   
   Y.DD.DDM.on('drag:drophit', function(e) {
	   var ndDrag = e.drag.get('node');
	   var ndDrop = e.drop.get('node');
	   if (ndDrop.get('tagName').toLowerCase() === 'table') {
		   ndDropParent = ndDrop.get('parentNode');
		   ndDrop = ndDrop.get('nextSibling');
		   if (ndDrop) {
			   ndDropParent.insertBefore(ndDrag, ndDrop);
		   } else {
			   ndDropParent.appendChild(ndDrag);
		   }
	   } else if (ndDrop.get('tagName').toLowerCase() === 'div' && ndDrop.hasClass('bmrcp-head')) {
		   ndDrop.get('nextSibling').prepend(ndDrag);
	   }
   });

	resizeTasks();
});

