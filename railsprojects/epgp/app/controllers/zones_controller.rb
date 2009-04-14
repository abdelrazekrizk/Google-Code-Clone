class ZonesController < ApplicationController
  before_filter :login_required, :except => %w[index show]
  def index
    @zones = Zone.find(:all, :order => 'name ASC')
  end
  
  def show
    @zone = Zone.find(params[:id])
    @bosses = Boss.find_all_by_zone_id(params[:id], :order => 'name ASC')
  end
  
  def adminindex
    @zones = Zone.find(:all, :order => 'name ASC')
  end
    
  def edit
    @zone = Zone.find(params[:id])
  end
  
  def update    
    z = Zone.find(params[:zone][:id])
    if(z.update_attributes(params[:zone]))
      flash[:notice] = "Updated successfully."
      redirect_to :action => 'adminindex'
    else
      flash[:error] = "Did not update successfully."
      redirect_to :action => 'edit'
    end
  end
  
  def destroy
    zone = Zone.find(params[:id])
    if(zone.bosses.length == 0)
      if(Zone.find(params[:id]).destroy)
        flash[:notice] = "Deleted successfully"
      else
        flash[:error] = "Deletion unsuccessful"
      end
    else
      flash[:notice] = "Deletion unsuccessful - Zone not empty"
    end
    redirect_to :action => 'adminindex'
  end
  
  def new
  end
  
  def create
    z = Zone.new
    if(z.update_attributes(params[:zone]))
      flash[:notice] = "Created successfully."
      redirect_to :action => 'adminindex'
    else
      flash[:error] = "Did not create successfully."
      redirect_to :action => 'create'
    end
  end
end
