function LSStompClient() {
	this.onmessageframe = function (frame) {};
	this.onopen = function () {};
	
	this.page = new PushPage();
    this.page.context.setDomain(document.domain);
	this.page.bind();
	this.page.onEngineCreation = function(engine) {
		engine.connection.setLSHost(document.domain); // (set the hostname when deploying on WEB SERVER)
		engine.connection.setLSPort(8080); // (set the port when deploying on WEB SERVER)
		engine.connection.setAdapterName("STOMP_RAW");
		engine.changeStatus("STREAMING");
	}
	this.page.createEngine("stompEngine", "../commons/lightstreamer", "SHARE_SESSION");	
}

LSStompClient.prototype.subscribe = function(channel) {
	var me = this;
	var group = [ channel ];
	var schema = [ "msg" ];

	// create a NonVisualTable, ro receive the potfolio updates
	this.table = new NonVisualTable(group, schema, "RAW");
	this.table.setSnapshotRequired(true);

	this.table.onItemUpdate = function(itemPos, updateInfo, itemName) {
		var msg = updateInfo.getNewValue("msg");
		var frame = {"msg": msg}
		me.onmessageframe(frame);
	};
	

	this.table.onStart = function() {
		me.onopen();
	};
	this.page.addTable(this.table, "stomp_table");
}

