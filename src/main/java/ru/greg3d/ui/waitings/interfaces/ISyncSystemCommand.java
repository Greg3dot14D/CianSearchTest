package ru.greg3d.ui.waitings.interfaces;

import java.util.function.Supplier;

public interface ISyncSystemCommand {
	public <T> T supply(Supplier<T> s);
}
