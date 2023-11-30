package com.ssf.homevisit.utils;

public interface Listener {
    interface dialogResponse{
        void OnResponse(boolean response);
    }
    interface onItemSelected{
        void OnResponse(int position);
    }
}
