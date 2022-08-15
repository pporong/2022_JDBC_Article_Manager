package com.KoreaIT.example.JAM.controller;

import java.util.Scanner;

public abstract class Controller {
	protected Scanner sc;

	public Controller(Scanner sc) {
		this.sc = sc;
	}

}