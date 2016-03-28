package com.onlinemarketing.config;

public class PathConfig {
	 private String root = ".";
		public PathConfig() {
			// TODO Auto-generated constructor stub
			  super();
			try {
				  this.root = this.getClass().getClassLoader().getResource("/").toURI().getPath();
				  root = root.replaceAll("classes/", "");
			} catch (Exception e) {
				  this.root = ".";
			}
		}
		
		 public String getRoot() {
			  return root;
		 }
}
