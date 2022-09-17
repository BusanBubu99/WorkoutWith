import requests
import kakaoapikey

#클라이언트에서 동네의 FULL 주소를 서버로 전송한다.
def findNearbyLocation(baseLocation, searchBase):
    townSearchKeyword = ""#"부산광역시 부산진구 부암동"  # Query
    townSearchKeyword = baseLocation

    #TODO
    #서버의 DB에 해당 주소에 대한 lat, lon정보가 있는지 확인한다. 있으면 request를 하지않고 바로 다음단계로 이동

    response = requests.get("https://apis.openapi.sk.com/tmap/pois?version=1&format=json&",
                            params={
                                "appKey": kakaoapikey.apikey,
                                            "searchKeyword": townSearchKeyword,
                                            "resCoordType": "WGS84GEO",
                                            "reqCoordType": "WGS84GEO",
                                            "count": 1
                            })

    #위도, 경도정보를 파싱한다.
    response = response.json()
    lat = response['searchPoiInfo']['pois']['poi'][0]['noorLat']
    lon = response['searchPoiInfo']['pois']['poi'][0]['noorLon']

    userTownLatLon = (lat, lon)  # 유저의 동네 위도 경도를 저장한다.

    #현재 열려있는 매칭큐의 동네정보 주소를 읽어와서 for문시작.
    range = 5000  # 5km

    #TODO 열려있는 매칭큐의 동네정보 주소
    #처음시작한 사람이 기준이됨
    openedMatchAddress = searchBase #이 리스트는 선착순으로 만들어짐

    link = "https://dapi.kakao.com/v2/local/search/keyword.json?y=" + \
        userTownLatLon[0]+"&x="+userTownLatLon[1]+"&radius="+str(range)

    for q in openedMatchAddress :
        response = requests.get(url=link, headers={
                            "Authorization": kakaoapikey.apiauth}, params="query="+q)
        response = response.json()
        count = len(response['documents'])
        if count != 0 :
            return q
    
    return None
