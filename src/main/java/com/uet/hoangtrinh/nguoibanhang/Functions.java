package com.uet.hoangtrinh.nguoibanhang;

import com.uet.hoangtrinh.GaApplication;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: TrinhHoang - UET
 * @description: Thuật toán di truyền (Genetic Algorithm–GA) và các hàm sử dụng.
 */
public class Functions {
    /**
     * Tạo Ngẫu nhiên một mảng.
     * @param n Só diểm trong 1 nghiệm
     * @return Mảng các diểm ngẫu nhiên
     */
    private int[] randomContent(int n) {
        int res[] = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = ThreadLocalRandom.current().nextInt(n);
        }
        return res;
    }

    /**
     * Tạo ngẫu nhiên quần thẻ với số lượng cho trước (GaApplication.NUMBER_IN_GROUP).
     */
    public void createGroup() {
        Entity entityTmp;
        for (int i = 0; i < GaApplication.NUMBER_IN_GROUP; i++) {
            entityTmp = new Entity(randomContent(GaApplication.amountNode));
            GaApplication.group.add(entityTmp);
        }
    }

    /**
     * Đánh giá các nghiệm trọng quần thể
     * sau đó sắp xếp các nghiệm theo điẻm đánh giá từ thấp đến cao.
     * Điểm đánh giá tính bằng tổng khoảng cách 2 điểm liền kề trong 1 nghiệm.
     */
    public void rating() {
        int scoreTmp;
        for (int i = 0; i < GaApplication.NUMBER_IN_GROUP; i++) {
            scoreTmp = 0; // Điểm đánh giá của quần thể thứ i
            // Tính điểm đánh giá
            for (int j = 0; j < GaApplication.amountNode - 1; j++) {
                scoreTmp += GaApplication.MAP[GaApplication.group.get(i).getContent()[j]]
                            [GaApplication.group.get(i).getContent()[j + 1]];
                GaApplication.group.get(i).setScore(scoreTmp);
            }
            // TH nghiệm có 2 điểm trùng nhau có điểm đánh giá cộng thêm x vào điểm đánh giá
            // x = Tổng số điểm trong bài toán * khoảng cách lớn nhất giwuax 2 điểm * 2
            for(int j = 0; j < GaApplication.amountNode - 1; j++) {
                for (int t = j + 1; t < GaApplication.amountNode; t++)
                    if (GaApplication.group.get(i).getContent()[j] == GaApplication.group.get(i).getContent()[t]) {
                        scoreTmp += GaApplication.amountNode * GaApplication.distanceMax * 2;
                        GaApplication.group.get(i).setScore(scoreTmp);
                    }
            }
        }
        sortGroup(GaApplication.group); // Sắp xếp danh sách nghiệm theo điểm đánh giá thấp -> cao
    }

    /**
     * Chọn các có
     */
    public void selected() {
        int nguongIndex = (int) (GaApplication.NUMBER_IN_GROUP * 50.0/100);
        for (int i = nguongIndex; i < GaApplication.NUMBER_IN_GROUP; i++) {
                GaApplication.group.set(i, new Entity(GaApplication.group.get(ThreadLocalRandom.current().nextInt(nguongIndex))));
        }
        sortGroup(GaApplication.group); // Sắp xếp danh sách nghiệm theo điểm đánh giá thấp -> cao
    }

    public void crossover() {
        for (int i = 0; i < GaApplication.NUMBER_IN_GROUP * 20.0/100; i++){
            int father = ThreadLocalRandom.current().nextInt(GaApplication.NUMBER_IN_GROUP);
            int mother = ThreadLocalRandom.current().nextInt(GaApplication.NUMBER_IN_GROUP);
            for (int j = 0; j < GaApplication.amountNode; j++)
                if (ThreadLocalRandom.current().nextInt(2) == 1) {
                    int temp = GaApplication.group.get(father).getContent()[j];
                    GaApplication.group.get(father).getContent()[j] = GaApplication.group.get(mother).getContent()[j];
                    GaApplication.group.get(mother).getContent()[j] = temp;
                }
        }
        sortGroup(GaApplication.group); // Sắp xếp danh sách nghiệm theo điểm đánh giá thấp -> cao
    }

    public void mutation() {
        for (int i = 0; i < GaApplication.NUMBER_IN_GROUP * 20/100.0; i++) {
            int index = ThreadLocalRandom.current().nextInt(GaApplication.NUMBER_IN_GROUP);
            int bit = ThreadLocalRandom.current().nextInt(GaApplication.amountNode);
            GaApplication.group.get(index).getContent()[bit] = ThreadLocalRandom.current().nextInt(GaApplication.amountNode);
        }
        sortGroup(GaApplication.group); // Sắp xếp danh sách nghiệm theo điểm đánh giá thấp -> cao
    }

    public boolean canStop() {
        for (int j = 0; j < GaApplication.amountNode - 1; j++)
            if (!GaApplication.GRAPH.
                    hasEdgeConnecting(GaApplication.group.get(0).getContent()[j],
                            GaApplication.group.get(0).getContent()[j + 1]))
                return false;
        return true;
    }

    /**
     * Sắp xếp danh sách theo điểm đánh giá thấp -> cao.
     * @param group Danh sách cần sắp xếp
     */
    private void sortGroup(List<Entity> group) {
        group.sort((o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()) * -1);
    }
}
