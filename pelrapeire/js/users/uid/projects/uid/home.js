
var pnl = new pv.Panel()
		.canvas("xbar")
		.width(964)
		.height(300)
		.bottom(10)
		.top(10)
		.left(10)
		.right(5);
pnl.add(pv.Dot)
		.data(xbardata.subgroupavgs)
		.left(function(d) {return this.index * 20;})
		.bottom(function(d){return d/2;})
	.add(pv.Line)
		.data([xbardata.xbarucl, xbardata.xbarucl])
		.left(function(d) {return this.index * 600;})
		.bottom(function(d) {return d/2;})
		.lineWidth(1)
	.add(pv.Line)
		.data([xbardata.xbarbar, xbardata.xbarbar])
		.left(function(d) {return this.index * 600;})
		.bottom(function(d) {return d/2;})
		.lineWidth(2)
pnl.add(pv.Rule)
			.left(0)
			.strokeStyle("#aaa")
pnl.add(pv.Rule)
			.bottom(0)
			.strokeStyle("#aaa")
pnl.render();

