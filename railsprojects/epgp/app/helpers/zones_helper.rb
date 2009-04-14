module ZonesHelper
  def zone_link(zone)
    link_to zone.name, :controller => 'zones', :action => 'show', :id => zone.id
  end
end
