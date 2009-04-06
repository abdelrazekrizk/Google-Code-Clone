<?php

/**
 * Roster form base class.
 *
 * @package    epgp
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseRosterForm extends BaseFormPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'id'             => new sfWidgetFormInputHidden(),
      'name'           => new sfWidgetFormInput(),
      'charclass'      => new sfWidgetFormPropelChoice(array('model' => 'Classes', 'add_empty' => false)),
      'charrace'       => new sfWidgetFormPropelChoice(array('model' => 'Races', 'add_empty' => false)),
      'ep'             => new sfWidgetFormInput(),
      'gp'             => new sfWidgetFormInput(),
      'priority'       => new sfWidgetFormInput(),
      'joined_on'      => new sfWidgetFormDateTime(),
      'is_active'      => new sfWidgetFormInputCheckbox(),
      'attendees_list' => new sfWidgetFormPropelChoiceMany(array('model' => 'Raids')),
    ));

    $this->setValidators(array(
      'id'             => new sfValidatorPropelChoice(array('model' => 'Roster', 'column' => 'id', 'required' => false)),
      'name'           => new sfValidatorString(array('max_length' => 255)),
      'charclass'      => new sfValidatorPropelChoice(array('model' => 'Classes', 'column' => 'id')),
      'charrace'       => new sfValidatorPropelChoice(array('model' => 'Races', 'column' => 'id')),
      'ep'             => new sfValidatorNumber(),
      'gp'             => new sfValidatorNumber(),
      'priority'       => new sfValidatorNumber(),
      'joined_on'      => new sfValidatorDateTime(),
      'is_active'      => new sfValidatorBoolean(),
      'attendees_list' => new sfValidatorPropelChoiceMany(array('model' => 'Raids', 'required' => false)),
    ));

    $this->validatorSchema->setPostValidator(
      new sfValidatorPropelUnique(array('model' => 'Roster', 'column' => array('name')))
    );

    $this->widgetSchema->setNameFormat('roster[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'Roster';
  }


  public function updateDefaultsFromObject()
  {
    parent::updateDefaultsFromObject();

    if (isset($this->widgetSchema['attendees_list']))
    {
      $values = array();
      foreach ($this->object->getAttendeess() as $obj)
      {
        $values[] = $obj->getRaidsId();
      }

      $this->setDefault('attendees_list', $values);
    }

  }

  protected function doSave($con = null)
  {
    parent::doSave($con);

    $this->saveAttendeesList($con);
  }

  public function saveAttendeesList($con = null)
  {
    if (!$this->isValid())
    {
      throw $this->getErrorSchema();
    }

    if (!isset($this->widgetSchema['attendees_list']))
    {
      // somebody has unset this widget
      return;
    }

    if (is_null($con))
    {
      $con = $this->getConnection();
    }

    $c = new Criteria();
    $c->add(AttendeesPeer::ROSTER_ID, $this->object->getPrimaryKey());
    AttendeesPeer::doDelete($c, $con);

    $values = $this->getValue('attendees_list');
    if (is_array($values))
    {
      foreach ($values as $value)
      {
        $obj = new Attendees();
        $obj->setRosterId($this->object->getPrimaryKey());
        $obj->setRaidsId($value);
        $obj->save();
      }
    }
  }

}
