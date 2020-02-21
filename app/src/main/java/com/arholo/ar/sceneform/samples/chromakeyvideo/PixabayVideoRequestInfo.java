package com.arholo.ar.sceneform.samples.chromakeyvideo;

import java.util.List;

public class PixabayVideoRequestInfo {

    private int total;
    private int totalHits;
    private List<PixabayVideoInfo> hits;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public List<PixabayVideoInfo> getHits() {
        return hits;
    }

    public void setHits(List<PixabayVideoInfo> hits) {
        this.hits = hits;
    }

    public PixabayVideoRequestInfo(int total, int totalHits, List<PixabayVideoInfo> hits) {
        this.total = total;
        this.totalHits = totalHits;
        this.hits = hits;
    }
}
