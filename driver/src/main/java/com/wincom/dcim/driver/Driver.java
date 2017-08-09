package com.wincom.dcim.driver;

public interface Driver {
	void received(Sender s, Object m);
	void datalink(Sender s);
	void initialized();
}
