<?php

require_once(sfConfig::get('sf_lib_dir').'/filter/base/BaseFormFilterPropel.class.php');

/**
 * EpgpAttendees filter form base class.
 *
 * @package    epgp
 * @subpackage filter
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormFilterGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseEpgpAttendeesFormFilter extends BaseFormFilterPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'raidid'   => new sfWidgetFormPropelChoice(array('model' => 'EpgpRaids', 'add_empty' => true)),
      'playerid' => new sfWidgetFormPropelChoice(array('model' => 'EpgpRoster', 'add_empty' => true)),
    ));

    $this->setValidators(array(
      'raidid'   => new sfValidatorPropelChoice(array('required' => false, 'model' => 'EpgpRaids', 'column' => 'id')),
      'playerid' => new sfValidatorPropelChoice(array('required' => false, 'model' => 'EpgpRoster', 'column' => 'id')),
    ));

    $this->widgetSchema->setNameFormat('epgp_attendees_filters[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'EpgpAttendees';
  }

  public function getFields()
  {
    return array(
      'raidid'   => 'ForeignKey',
      'playerid' => 'ForeignKey',
      'id'       => 'Number',
    );
  }
}
