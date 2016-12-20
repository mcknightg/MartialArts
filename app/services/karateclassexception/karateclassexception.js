'use strict';

//  Martialarts Karateclassexception Service
catwalkApp.factory('MartialartsKarateclassexception', ['MartialartsBaseService',function (MartialartsBaseService) {
    var entityUrl = MartialartsBaseService.getEntityUrl('karateclassexception');
    return MartialartsBaseService.getResource(entityUrl,{},{
        'columns':{method: 'POST', params:{},url:entityUrl + 'columns'},
        'api':{method: 'POST', params:{},url:entityUrl + 'api'},
        'schema':{method: 'POST', params:{},url:entityUrl + 'schema'}
    });
}
]);
