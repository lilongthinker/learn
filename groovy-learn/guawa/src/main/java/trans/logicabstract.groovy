package trans

/**
 * User: dingyue
 * Date: 13-3-18
 * Time: 上午10:24
 */
class logicabstract {
}


class Customer{
    Long custId
    String type
}

class Campaign{

}

enum CampaignTypeEnum {
    ACTIVITY
}

abstract class AbstractSetting{
    CampaignTypeEnum campaignTypeEnum
}

class AreaSetting extends AbstractSetting{}

class BidSetting  extends AbstractSetting{

    static BidSetting ACTIVITY_DEFAULT = new BidSetting(campaignTypeEnum: CampaignTypeEnum.ACTIVITY, dayLimit: max)

    static long min = 3000L
    static long max = 200000000L

    long dayLimit

}



class Adgroup{
    Long id
}


