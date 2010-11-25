
var pnl = new pv.Panel()
		.canvas("xbar")
		.width(934)
		.height(300)
		.bottom(20)
		.top(20)
		.left(20)
		.right(20);
		
pnl.add(pv.Dot)
		.data(xbardata.subgroupavgs)
		.left(function(d) {return this.index * 20 + 10;})
		.bottom(function(d){return d/2;});
		
var rlLeft = pnl.add(pv.Rule)
	.left(0)
	.strokeStyle("#aaa");
rlLeft.add(pv.Label)
	.text("hours")
	.left(-5)
	.top(100)
	.textAngle(-Math.PI/2);
		
var rlRight = pnl.add(pv.Rule)
	.right(0)
	.strokeStyle("#aaa");
rlRight.add(pv.Label)
	.text("days")
	.right(-15)
	.top(100)
	.textAngle(-Math.PI/2);
				
var hrule = pnl.add(pv.Rule)
			.data([0, xbardata.xbarbar, xbardata.xbarucl])
			.bottom(function(d) {return d/2})
			.strokeStyle("#aaa")
			.add(pv.Label)
				.text(function(d) {return Math.round(d);})
				.textAlign("right")
				.textBaseline("middle");
hrule.add(pv.Label)
	.text(function(d) {return Math.round(d/24);})
	.right(0)
	.textAlign("left")
	.textBaseline("middle");
	
pnl.add(pv.Label)
	.left(934/2)
	.top(25)
	.textAlign("center")
	.font(15 + "px sans-serif")
	.text("Work In Progress History");

pnl.render();

