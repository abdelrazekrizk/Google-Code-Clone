module DecaysHelper
  def decay_link(decay)
    link_to decay.performed_on, :controller => 'decays', :action => 'show', :id => decay.id
  end
  
  def admin_decays_table(decays)
    t = "<table>"
    t += "<thead>"
    t += "<tr>"
    ["Time"].each do |h|
    t += "<th>" + h + "</th>"
    end
    t += "</tr>"
    t += "</thead>"
    t += "<tbody>"
    decays.each do |d|
      t += "<tr>"
      [d.performed_on].each do |v|
        t += "<td>#{v.to_s}</td>"
      end
      t += "<td>"
      t += link_to "Destroy", {:action => :destroy, :id => d.id}, :confirm => "Are you sure?"
      t += "</td>"
      t += "</tr>"
    end
    t += "</tbody>"
    t += "</table>"
    t += link_to "New", :action => :new
    return t
  end
end
