/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Arne Kepp, The Open Planning Project, Copyright 2008
 */
package org.geowebcache.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geowebcache.GeoWebCacheException;
import org.geowebcache.layer.TileLayer;
import org.geowebcache.layer.TileLayerDispatcher;
import org.geowebcache.tile.Tile;
import org.geowebcache.util.ServletUtils;

public abstract class Service {
    
    private String pathName = null;
    
    protected static TileLayerDispatcher tlDispatcher = null;
    
    public Service(String pathName){
        this.pathName = pathName;
    }
    
    public static void setTileLayerDispatcher(TileLayerDispatcher tlDispatcher) {
        Service.tlDispatcher = tlDispatcher;
    }
    
    /**
     * Whether this service can handle the given request
     * 
     * @return
     */
    public boolean handlesRequest(HttpServletRequest request) {
        return request.getPathInfo().equalsIgnoreCase(pathName);
    }
    
    public String getPathName(){
        return new String(pathName);
    }
    
    public Tile getTile(HttpServletRequest request, HttpServletResponse response) 
    throws GeoWebCacheException {
        throw new ServiceException (
                "Service for " + pathName  + " needs to override "
                +"getTile(HttpSerlvetRequest)" );
    }
    
    public void handleRequest(TileLayerDispatcher tLD, Tile tile) 
    throws GeoWebCacheException {
        throw new RuntimeException(
                "Service for " + pathName  + " needs to override "
                +"handleRequest(TileLayer, HttpServletRequest, ServiceRequest, "
                +"HttpServletResponse)" );
    }
    
    protected String getLayersParameter(HttpServletRequest request) throws ServiceException {
    	String layers = ServletUtils.stringFromMap(request.getParameterMap(), "layers");
    	if(layers == null) {
    		throw new ServiceException("Unable to parse layers parameter from request.");
    	} 
    	return layers;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof Service) {
            Service other = (Service) obj;
            if(other.pathName != null 
                    && other.pathName.equalsIgnoreCase(pathName)) {
                return true;
            }
        }
        
        return false;
    }
    
    public int hashCode() {
        return pathName.hashCode();
    }
    
}
