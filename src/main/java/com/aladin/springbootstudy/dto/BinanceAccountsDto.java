package com.aladin.springbootstudy.dto;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Data
public class BinanceAccountsDto {

    public Integer feeTier;
    public Boolean canTrade;
    public Boolean canDeposit;
    public Boolean canWithdraw;
    public Integer updateTime;
    public Boolean multiAssetsMargin;
    public String totalInitialMargin;
    public String totalMaintMargin;
    public String totalWalletBalance;
    public String totalUnrealizedProfit;
    public String totalMarginBalance;
    public String totalPositionInitialMargin;
    public String totalOpenOrderInitialMargin;
    public String totalCrossWalletBalance;
    public String totalCrossUnPnl;
    public String availableBalance;
    public String maxWithdrawAmount;
    public List<Asset> assets;
    public List<Position> positions;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();
    @Data
    public static class Asset {

        public String asset;
        public String walletBalance;
        public String unrealizedProfit;
        public String marginBalance;
        public String maintMargin;
        public String initialMargin;
        public String positionInitialMargin;
        public String openOrderInitialMargin;
        public String crossWalletBalance;
        public String crossUnPnl;
        public String availableBalance;
        public String maxWithdrawAmount;
        public Boolean marginAvailable;
        public Long updateTime;
        private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();
    }

    @Data
    public static class Position {

        public String symbol;
        public String initialMargin;
        public String maintMargin;
        public String unrealizedProfit;
        public String positionInitialMargin;
        public String openOrderInitialMargin;
        public String leverage;
        public Boolean isolated;
        public String entryPrice;
        public String maxNotional;
        public String bidNotional;
        public String askNotional;
        public String positionSide;
        public String positionAmt;
        public String notional;
        public String isolatedWallet;
        public Long updateTime;
        private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();
    }
}

