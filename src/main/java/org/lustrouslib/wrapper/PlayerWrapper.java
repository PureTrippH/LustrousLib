package org.lustrouslib.wrapper;

import org.bukkit.entity.Player;
import org.lustrouslib.menu.interfaces.GUIMenu;
import org.lustrouslib.menu.interfaces.MenuClient;
import org.lustrouslib.menu.interfaces.PlayerState;

import java.util.Stack;

public class PlayerWrapper implements MenuClient {
    Player p;
    private Stack<GUIMenu> menuStack;
    private PlayerState currState;


    public PlayerWrapper(Player p) {
        this.p = p;
        this.menuStack =  new Stack<GUIMenu>();
    }

    @Override
    public void pushMenu(GUIMenu menu) {
        menuStack.push(menu);
        menu.open(p);
    }

    @Override
    public void popMenu() {
        if (menuStack.size() <= 1) {
            menuStack.clear();
            return;
        }
        menuStack.pop();
        menuStack.peek().open(p);
    }

    public GUIMenu getCurrMenu() {
        return menuStack.peek();
    }

    @Override
    public Player getPlayer() {
        return p;
    }

    public void setState(PlayerState state) {
        currState = state;
    }

    public void clearState() {
        currState = null;
    }

    public PlayerState getState() {
        return currState;
    }
}