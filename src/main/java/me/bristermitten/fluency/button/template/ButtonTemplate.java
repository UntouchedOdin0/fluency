package me.bristermitten.fluency.button.template;

import me.bristermitten.fluency.Fluency;
import me.bristermitten.fluency.button.ButtonBuilder;
import me.bristermitten.fluency.button.MenuButton;
import me.bristermitten.fluency.button.click.ClickHandler;
import me.bristermitten.fluency.button.click.Handlers;
import me.bristermitten.fluency.data.ButtonHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ButtonTemplate<T> extends ButtonHolder {
	public static final Material DEFAULT_TYPE = Material.AIR;

	private final Fluency fluency;
	private boolean requiresPlayer;
	private Function<T, String> nameFunction;
	private Function<T, Material> typeFunction;
	private Function<T, List<String>> loreFunction;
	private Function<Player, T> sourceFunction;
	private ClickHandler handler;

	public ButtonTemplate(Fluency fluency) {
		this.fluency = fluency;
		nameFunction = any -> null;
		loreFunction = any -> new ArrayList<>();
		typeFunction = any -> DEFAULT_TYPE;
		handler = Handlers.DO_NOTHING;
	}

	@Nullable
	public Function<Player, T> sourceFunction() {
		return sourceFunction;
	}

	public void sourceFunction(Function<Player, T> sourceFunction) {
		this.sourceFunction = sourceFunction;
	}

	public MenuButton create(Fluency f, T t) {
		return create(f.buildButton(), t);
	}

	public MenuButton create(ButtonBuilder builder, T t) {
		return builder
				.type(typeFunction.apply(t))
				.name(nameFunction.apply(t))
				.lore(loreFunction.apply(t))
				.onClick().action(handler).done()
				.build();
	}


	public Function<T, List<String>> loreFunction() {
		return loreFunction;
	}

	public void loreFunction(Function<T, List<String>> loreFunction) {
		this.loreFunction = loreFunction;
	}

	public Function<T, String> nameFunction() {
		return nameFunction;
	}

	public void nameFunction(Function<T, String> nameFunction) {
		this.nameFunction = nameFunction;
	}

	public Function<T, Material> typeFunction() {
		return typeFunction;
	}

	public void typeFunction(Function<T, Material> typeFunction) {
		this.typeFunction = typeFunction;
	}

	public void requiresPlayer(boolean requiresPlayer) {
		this.requiresPlayer = requiresPlayer;
	}

	public boolean requiresPlayer() {
		return requiresPlayer;
	}

	public void handler(ClickHandler handler) {
		this.handler = handler;
	}

	@Override
	public MenuButton get() {
		if (requiresPlayer || sourceFunction == null) {
			return null;
		}
		return create(fluency, sourceFunction.apply(null));
	}

	public MenuButton getFromViewer(Player viewer) {
		return create(fluency, sourceFunction.apply(viewer));
	}

	@Override
	public boolean has() {
		return true;
	}
}
