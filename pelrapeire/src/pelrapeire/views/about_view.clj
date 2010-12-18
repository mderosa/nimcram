(ns pelrapeire.views.about-view
  (:use clojure.contrib.trace))

(def purpose-answer 
     [:p "The purpose of the application is to get people to break tasks up in to small pieces 
and deliver those bits of tasks to thier final destination quickly. Right now, there is feedback
for project members as a whole to keep track of their speed in delivering but that shouldn't be
interpreted as an intent to make people work faster.  The proper intent here is to start getting 
very quick feedback loops between task executors and the task consumers. Over the long term as 
groups get used to these quick feedback loops wasted efforts become identified very early and they
are pruned away.  Efficiency goes up overall."])

(def using-lists-answer
     [:p "Start by putting tasks in 'proposed' section.  To start it's fine to just get a list of
sketchy, basic task in this section. Some of these tasks wont every get worked on.  There will be more
important things that come up.  Some of the tasks however will be important enough to start work on.
As soon as anyone starts serious work on a task it should be moved into the 'work in progress' section.
For software development, as an example, as soon as someone starts to fill out the specification section in
detail enough for implementation, the task should be moved.  While the work is in progress one person, but
probably several people will work on aspects of the task.  When the task is done and delivered, and the
delivery is the important part here, the task can be moved into the 'delivered to user' list.  Again,
to use software development as an example, the task is coded and tested as a work in progress. But
the task is not moved until the functionality of that task is delivered to a live production site."])

(def reading-graph-answer
     [:p "Once enough tasks have been delivered data will start to show up on the progress history
graph.  The graph will show the average time for a task to move through your whole process.  That will
be your loop time.  This loop time should be as small as possible and that will come about through a
combination of making tasks smaller and later by removing waste and working smarter.  The warning line
will show how stable the process is.  Generally the closer the warning line to the average line the better
and the fewer excursions beyond the waring limit the better.  When the warning limit is exceeded, team
should consult the list of delivered tasks, determine what aspects of the process caused the excursion, and
make changes to prevent those influences on the process from happening again."])

(defn show [map-data]
  (let [js nil
	css nil
	title "about"
	content 
	[:div {:class "infoblock"}
	[:h2 "The purpose of the application"]
	 purpose-answer
	[:h2 "Using the three lists on each project's home page"]
	 using-lists-answer
	[:h2 "Reading the 'Work In Progress History' graph"]
	 reading-graph-answer
	 [:p]]]
    {:js js :css css :title title :content content}))