(function () {
    "use strict";
    angular.module('progesteroneLevel')
        .factory('pregnancyService', function pregnancyService($http){
            var baseUrl = 'http://localhost:8080/pregnancies/';

            return {
                get: function(){
                    return $http.get(baseUrl)
                        .then(response => response.data._embedded.pregnancies);
                },
                find: function(id){
                    return $http.get(baseUrl + id)
                        .then(response => response.data);
                },
                create: function(pregnancy){
                    return $http.post(baseUrl, pregnancy);
                },
                update: function(pregnancies){
                    return $http.put(pregnancies._links.self.href, pregnancies);
                },
                destroy: function(id){
                    contacts.splice(id, 1);
                }
            };
        });
})();