package com.github.xtwxy.play.websockets.jclient;

import java.net.URISyntaxException;

import io.socket.client.*;
import io.socket.emitter.*;

public class Main {

	public static void main(String[] args) throws URISyntaxException {

		if (args.length == 0) {
			System.out.println("Usage: <cmd> <url>");
			return;
		}

		Socket socket = IO.socket(args[0]);
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				socket.emit("foo", "hi");
				System.out.println("(event => foo, message => hi) is sent.");
			}

		}).on("event", new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.printf("(event => event, message => %s) is sent.\n", args);
				socket.emit("event", "greetings!");
			}

		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.printf("(event => %S, message => %s) is sent.\n", Socket.EVENT_DISCONNECT, args);
				System.exit(0);
			}

		});
		socket.connect();

    	System.out.println("post connect()...");
	}
}
