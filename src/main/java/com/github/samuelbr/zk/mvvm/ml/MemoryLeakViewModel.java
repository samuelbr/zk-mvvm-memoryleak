package com.github.samuelbr.zk.mvvm.ml;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.impl.BinderImpl;
import org.zkoss.bind.tracker.impl.TrackerImpl;
import org.zkoss.zul.ListModelList;

public class MemoryLeakViewModel {

	private ListModelList<Pojo> data = new ListModelList<Pojo>();

	private BinderImpl binder;

	public MemoryLeakViewModel() {
		reload();
	}

	@Init
	public void init(@ContextParam(ContextType.BINDER) Binder binder) {
		this.binder = (BinderImpl) binder;
	}

	public ListModelList<Pojo> getModel() {
		return data;
	}

	public int getComponentsCount()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		TrackerImpl tracker = (TrackerImpl) binder.getTracker();
		Field field = TrackerImpl.class.getDeclaredField("_compMap");
		field.setAccessible(true);

		LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) field.get(tracker);

		return map.size();
	}

	@Command
	@NotifyChange("*")
	public void doMemoryLeak() {
		reload();
	}
	
	private void reload() {
		data.clear();
		for (int a=0; a<100; a++) {
			data.add(new Pojo("Test - "+a));
		}
	}

	public static class Pojo {
		private String data;

		public Pojo(String data) {
			this.data = data;
		}

		public void setData(String data) {
			this.data = data;
		}

		public String getData() {
			return data;
		}

	}
}
